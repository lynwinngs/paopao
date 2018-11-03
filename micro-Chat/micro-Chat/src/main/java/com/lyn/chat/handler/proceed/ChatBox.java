package com.lyn.chat.handler.proceed;

import com.lyn.chat.protocal.ChatProtocol;

public interface ChatBox extends Runnable {
    public void chating();
    public void setProtocol(ChatProtocol protocol);
    public ChatProtocol getProtocol();
    void dealQue(ChatProtocol protocol);

    void toDie();
}
