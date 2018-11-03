package com.lyn.chat.handler.proceed;

import com.lyn.chat.protocal.ChatProtocol;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 建立连接，发送报文
 */
public class ChatBoxOut extends AbstractChatBox {

    private ObjectOutputStream oo = null;
    public ChatBoxOut() {
    }

    @Override
    public void run() {
        try {
            readLock.lock();
            if(socket!=null || protocol == null || isAlive) return;
            socket=new Socket(protocol.getRecipientIP(),protocol.getRecipientPort());
            AbstractChatBox.chatBoxeIns.put(protocol.getRecipientNum(),this);

            oo = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
            String msg = protocol.getMessage();
            readLock.unlock();
            writeLock.lock();
            this.protocolQueue.put(msg);
            writeLock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            readLock.unlock();
            writeLock.unlock();
        }
        this.chating();
    }

    @Override
    public void  chating() {
        for(;;) {
            try {
                writeLock.lock();
                String msg = this.protocolQueue.take();
                System.out.println("发送消息：：：：："+msg);
                protocol.setMessage(msg);
                oo.writeObject(protocol);
                oo.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                writeLock.unlock();
            }
        }
    }


    @Override
    public void toDie() {
        try {
            if(oo!=null){
                oo.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            if(socket!=null)
              socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            oo=null;
            socket=null;
            this.isAlive=false;
        }
    }
}
