package com.lyn.chat.handler.process;

import com.lyn.chat.protocal.ChatProtocol;

public enum  ChatHandler {
    LOGIN( new ChatLogin()),
    TRANSMIT( new ChatTransmit()),
    LOGOUT( new ChatLogout());

    private ChatProcess process;

    private ChatHandler(ChatProcess process){
        this.process=process;
    }
    public ChatProcess getProcess() {
        return process;
    }
}
