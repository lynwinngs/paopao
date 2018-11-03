package com.lyn.chat.handler.process;

import com.lyn.chat.handler.proceed.ChatBox;
import com.lyn.chat.protocal.ChatProtocol;
import com.lyn.chat.protocal.ProtocolConstant;
import com.lyn.chat.server.ChatServer;
import com.lyn.chat.server.ChatUtils;

/**
 * 登录：
 * 获取发送者IP和端口，保存；
 * 给发送者返回登录成功报文。
 */
public class ChatLogin extends ChatProcess {

    @Override
    public void run() {
        ChatProtocol protocol = getProtocol();
        if(protocol==null) return;
        String ip = protocol.getSenderIP();
        int port = protocol.getSenderPort();
        String num = protocol.getSenderNum();
        ChatUtils.clientMap.put(num,ip+":"+port);



        ChatProtocol protocolSend = new ChatProtocol(ChatServer.SERVER_IP,ChatServer.SERVER_PORT,
                ip,port, ProtocolConstant.CODE_RETURN,null,null, ChatUtils.getClientMapList());

        System.out.println("登录返回报文："+protocolSend);
        ChatHandler handler =ChatHandler.valueOf(ProtocolConstant.CODE_TRANSMIT);
        handler.getProcess().setProtocol(protocolSend);
        handler.getProcess().setSocket(this.getSocket());
        ChatUtils.poolExecutor.execute(handler.getProcess());
        return;
    }
}
