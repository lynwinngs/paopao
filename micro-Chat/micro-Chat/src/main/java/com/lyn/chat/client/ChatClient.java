package com.lyn.chat.client;

import com.lyn.chat.handler.proceed.AbstractChatBox;
import com.lyn.chat.handler.proceed.ChatBox;
import com.lyn.chat.handler.proceed.ChatBoxOut;
import com.lyn.chat.protocal.ChatProtocol;
import com.lyn.chat.protocal.ProtocolConstant;
import com.lyn.chat.server.ChatServer;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

/**
 * 客户端-发送
 * 1、登录，向服务端发送等登录请求，根据返回报文更新在线用户列表
 * 2、根据好友QQ号，发送消息。向另外一个客户端建立了Socket连接以后，保存该Socket，此次会话后续
 * 都使用该Socket向此好友发送消息。直到我方发送“BYE”消息（命令）。
 * 3、获取本地存储的在线用户列表。（还没做好）
 * 4、登出。(还没做好。)
 */
public class ChatClient implements Runnable{
    public static final String Client_IP = "127.0.0.1";
    public static int acceptPort ;
    private final ChatFan fan;
    private boolean isAlive=false;
    private HashMap<String,String> friend = this.getFan().getFriend();


    public ChatFan getFan() {
        return fan;
    }

    public ChatClient(int acceptPort, ChatFan fan) {
        this.acceptPort = acceptPort;
        this.fan = fan;
    }

    private void login() throws Exception {
        System.out.println("开始登录");
        ChatProtocol protocol = new ChatProtocol(Client_IP,acceptPort,
                ChatServer.SERVER_IP,ChatServer.SERVER_PORT, ProtocolConstant.CODE_LOGIN,
                fan.getFanNum(),"","");
        ChatProtocol returnProtocol = send(protocol);
        if(returnProtocol!=null && ProtocolConstant.CODE_RETURN.equals(returnProtocol.getCode())){
            String fansTmp = returnProtocol.getMessage();
            if (fansTmp!=null) {
                String[] fansINfo = fansTmp.split(",");
                for(String fanInfo : fansINfo){
                    String[] info=fanInfo.split(":");
                    if(info.length!=3) continue;
                    friend.put(info[0],info[1]+":"+info[2]);
                }
            }
            isAlive = true ;
        }else{
            System.out.println("登录失败");
            throw new Exception("登录失败");
        }

    }

    private void talk(String[] readLine) {
        try {
            String recipientNum = readLine[0];
            String message =  readLine[1];

            ChatBox box = AbstractChatBox.chatBoxeIns.get(recipientNum);
            if(ProtocolConstant.CODE_BYE.equals(message)){
                if (box==null){
                    return;
                }else if(((ChatBoxOut) box).isAlive()){
                    box.toDie();
                }
                AbstractChatBox.chatBoxeIns.remove(recipientNum);
                return;
            }
            String[] address = friend.get(readLine[0]).split(":");
            if(address.length!=2) System.out.println("好友地址不合法，不能发送消息哦！");;

            ChatProtocol protocol = new ChatProtocol(Client_IP,acceptPort,
                    address[0],Integer.parseInt(address[1]),ProtocolConstant.CODE_TRANSMIT,
                    fan.getFanNum(),recipientNum,message);

            if(box==null) {
                box = new ChatBoxOut();
                box.setProtocol(protocol);
                AbstractChatBox.executorService.execute(box);
            }else {
                box.dealQue(protocol);
            }

        /*ChatProtocol returnProtocol = send(protocol);
        String returnMessage = returnProtocol.getMessage();
        System.out.println(returnMessage);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
    @Override
    public void run() {
        boolean isLoop = true;
         while(isLoop){
            try {
                if(!isAlive) this.login();
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String msg = reader.readLine();
                switch (msg){
                    case ProtocolConstant.CODE_LIST: {
                        this.getFan().getFriendList();
                        break;
                    }
                    case ProtocolConstant.CODE_LOGOUT: {
                        this.logout();
                        isLoop=false;
                        break;
                    }
                    default:{
                        String[] readLine = msg.split("#");
                        if(readLine.length<2) {
                            System.out.println("格式：recipientNum#Message");
                            continue;
                        }
                        if(!friend.containsKey(readLine[0])) {
                            System.out.println(readLine[0]+"不在线哦！");
                            continue;
                        }
                        this.talk(readLine);
                    }
                }

            } catch (Exception e) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void logout() {
    }

    public ChatProtocol send(ChatProtocol protocol) {
        if (protocol == null) return null;
        System.out.println("客户端发送请求：" + protocol.toString());
        String ip = protocol.getRecipientIP();
        int port = protocol.getRecipientPort();

        Socket socket = null;
        ObjectOutputStream oo = null;
        ObjectInputStream objTmp = null;
        ChatProtocol returnProtocol = null;
        try {
            socket = new Socket(ip, port);
            oo = new ObjectOutputStream(socket.getOutputStream());
            oo.writeObject(protocol);
            oo.flush();

            objTmp = new ObjectInputStream(socket.getInputStream());

            returnProtocol = (ChatProtocol) objTmp.readObject();
            System.out.println("客户端接收：" + returnProtocol.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                objTmp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (oo != null) {
                oo = null;
            }
            if (objTmp != null) {
                objTmp = null;
            }
            if(socket!=null){
                socket=null;
            }
        }
        return returnProtocol;
    }
}
