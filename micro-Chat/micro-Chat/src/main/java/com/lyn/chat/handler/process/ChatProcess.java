package com.lyn.chat.handler.process;

import com.lyn.chat.protocal.ChatProtocol;

import java.net.Socket;

/**
 * 报文处理的基类
 */
public abstract class ChatProcess implements Runnable{
    private Socket socket=null;
    private ChatProtocol protocol = null;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setProtocol(ChatProtocol protocol) {
        this.protocol = protocol;
    }

    public ChatProtocol getProtocol() {
        return protocol;
    }

    public String getSenderIP(){
        return socket.getInetAddress().getHostAddress();
    }
    public int getSenderPort(){
        return socket.getPort();
    }
}
