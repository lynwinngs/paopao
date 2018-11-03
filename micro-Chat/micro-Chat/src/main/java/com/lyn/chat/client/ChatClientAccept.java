package com.lyn.chat.client;

import com.lyn.chat.handler.proceed.AbstractChatBox;
import com.lyn.chat.handler.proceed.ChatBox;
import com.lyn.chat.handler.proceed.ChatBoxIn;
import com.lyn.chat.protocal.ChatProtocol;
import com.lyn.chat.protocal.ProtocolConstant;
import com.lyn.chat.protocal.ProtocolUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * 客户端-接收
 * 除客户端给服务器发送请求的返回报文以外，所有报文由该线程接收。
 * 1、接收服务器推送的所有在线用户列表，更新本地存储。
 * 2、接收消息，根据不同QQ号转发给不同的Socket处理线程处理，
 * 如果该Socket处理线程还不存在，则新建一个，把报文转发给该线程，等待处理。
 * 如果已经存在则把收到的消息放入到该线程的阻塞队列中，等待读取。
 */
public class ChatClientAccept implements Runnable {

    private ServerSocket serverSocket;
    private int port;
    private final ChatFan fan;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChatClientAccept(ChatFan fan) throws IOException {
        serverSocket = new ServerSocket(0);
        port = serverSocket.getLocalPort();
        this.fan = fan;
    }

    public ChatFan getFan() {
        return fan;
    }

    @Override
    public void run() {
        if(serverSocket==null) return;
        Socket socket = null;
        ChatProtocol chatProtocol = null;
        ChatBox box = null;
        for(;;){
            try {
                socket = serverSocket.accept();
                chatProtocol = ProtocolUtils.getProtocol(socket);
                if(chatProtocol==null || chatProtocol.getMessage()==null) continue;
                if(chatProtocol.getCode()!=null && ProtocolConstant.CODE_LIST.equals(
                        chatProtocol.getCode()) ){
                    this.refresh(chatProtocol.getMessage());
                }
                if(AbstractChatBox.chatBoxeIns.get(chatProtocol.getSenderNum())!=null){
                    box = AbstractChatBox.chatBoxeIns.get(chatProtocol.getSenderNum());
                    box.dealQue(chatProtocol);
                }else{
                    box = new ChatBoxIn();
                    ((ChatBoxIn) box).setSocket(socket);
                    box.setProtocol(chatProtocol);
                    AbstractChatBox.executorService.execute(box);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                socket = null;
                chatProtocol=null;
                box=null;
            }
        }

    }

    private void refresh(String message) {
        HashMap<String,String> friend = new HashMap<>();
        String[] fansINfo = message.split(",");
        for(String fanInfo : fansINfo){
            String[] info=fanInfo.split(":");
            if(info.length!=3) continue;
            friend.put(info[0],info[1]+":"+info[2]);
        }
        synchronized (fan.getFriend()){
            fan.setFriend(friend);
        }
        friend=null;
    }
}
