package com.lyn.chat.handler.proceed;

import com.lyn.chat.protocal.ChatProtocol;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractChatBox implements ChatBox{
    public static ExecutorService executorService = Executors.newFixedThreadPool(4);
    public static HashMap<String, ChatBox> chatBoxeIns = new HashMap<>();
    public static HashMap<String, ChatBox> chatBoxeOuts = new HashMap<>();
    protected LinkedBlockingQueue<String> protocolQueue = new LinkedBlockingQueue<String>();
    protected ChatProtocol protocol=null;
    protected boolean isAlive =false;
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected Lock readLock=lock.readLock();
    protected Lock writeLock=lock.writeLock();

    public LinkedBlockingQueue<String> getProtocolQueue() {
        return protocolQueue;
    }

    public void setProtocolQueue(LinkedBlockingQueue<String> protocolQueue) {
        this.protocolQueue = protocolQueue;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    protected Socket socket=null;


    @Override
    public ChatProtocol getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(ChatProtocol protocol) {
        this.protocol=protocol;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    @Override
    public void dealQue(ChatProtocol protocol) {
        try {
            writeLock.lock();
            this.protocolQueue.put(protocol.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            writeLock.unlock();
        }
    }
}
