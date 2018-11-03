package com.lyn.chat.server;

import com.lyn.chat.protocal.ChatProtocol;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatUtils {
    public static final ExecutorService poolExecutor = Executors.newFixedThreadPool(4);
    public static final HashMap<String,String> clientMap = new HashMap<String,String>();
    public static String getClientMapList(){
        StringBuilder sb = new StringBuilder();
        ChatUtils.clientMap.forEach((k,v)->{
            sb.append(k+":"+v+",");
        });
        return  sb.toString();
    }
}
