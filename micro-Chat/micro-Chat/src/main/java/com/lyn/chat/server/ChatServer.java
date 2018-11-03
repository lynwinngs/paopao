package com.lyn.chat.server;

import com.lyn.chat.handler.process.ChatHandler;
import com.lyn.chat.protocal.ChatProtocol;
import com.lyn.chat.protocal.ProtocolConstant;
import com.lyn.chat.protocal.ProtocolUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 服务端（主类）
 * 接收客户端登录请求，返回在线用户列表。
 * 定时推送在线用户列表给所有客户端
 *
 */
public class ChatServer implements Runnable{
    public static final String SERVER_IP = "127.0.0.1";
    public static final int SERVER_PORT = 6666;
    private ServerSocket serverSocket = null;

    private void init(){
        if(serverSocket!=null) return;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ChatServer(){
        init();
    }

    @Override
    public void run() {
        if(serverSocket== null ){
            return;
        }
        Socket socket = null;
        ChatProtocol chatProtocol = null;
        while (true){
            System.out.println("开始接收请求"+chatProtocol);
            try {
                socket = serverSocket.accept();
                chatProtocol = ProtocolUtils.getProtocol(socket);
                String code = chatProtocol.getCode();
                System.out.println("服务端接收请求："+chatProtocol.toString());
                ChatHandler handler =ChatHandler.valueOf(code);
                handler.getProcess().setProtocol(chatProtocol);
                handler.getProcess().setSocket(socket);
                ChatUtils.poolExecutor.execute(handler.getProcess());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally {
                socket=null;
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new ChatServer()).start();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ChatUtils.clientMap.forEach((k, v) -> {
                    try {
                        String[] address = v.split(":");
                        if (address.length == 2) {
                            ChatProtocol protocolSend = new ChatProtocol(ChatServer.SERVER_IP, ChatServer.SERVER_PORT,
                                    address[0], Integer.parseInt(address[1]), ProtocolConstant.CODE_LIST, null,
                                    null, ChatUtils.getClientMapList());
                            Socket socket = new Socket(address[0], Integer.parseInt(address[1]));
                            ChatHandler handler = ChatHandler.valueOf(ProtocolConstant.CODE_TRANSMIT);
                            handler.getProcess().setProtocol(protocolSend);
                            handler.getProcess().setSocket(socket);
                            ChatUtils.poolExecutor.execute(handler.getProcess());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,100000,100000);
    }
}
