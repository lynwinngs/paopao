package com.lyn.chat.handler.process;

import com.lyn.chat.protocal.ChatProtocol;
import com.lyn.chat.server.ChatUtils;

import java.io.*;
import java.net.Socket;

/**
 * 报文发送：
 * 获取接收者IP和端口号；
 * 如果是推送消息建立新socket，如果是登录返回，使用原socket
 * 发送报文。
 */
public class ChatTransmit extends ChatProcess {

    public ChatTransmit() {}

    @Override
    public void run() {
        ChatProtocol protocol = getProtocol();
        if(protocol==null) return;
        Socket socket = null;
        ObjectOutputStream oo=null;
        try {
            if (this.getSocket() == null) {
                socket=new Socket(this.getProtocol().getRecipientIP(),this.getProtocol().getRecipientPort());
            }else{
                socket = this.getSocket();
            }
            oo = new ObjectOutputStream(socket.getOutputStream());
            oo.writeObject(protocol);
            oo.flush();
            oo.close();

        } catch (IOException e) {
        }finally {
            if(oo!=null){
                oo=null;
            }
            socket=null;
        }
    }
}
