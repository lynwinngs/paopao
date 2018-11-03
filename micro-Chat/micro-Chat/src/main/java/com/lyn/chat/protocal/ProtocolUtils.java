package com.lyn.chat.protocal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ProtocolUtils {
    public static ChatProtocol getProtocol(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objTmp = null;
        ChatProtocol chatProtocol = null;
        try {
            objTmp =  new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            chatProtocol = (ChatProtocol) objTmp.readObject();
        }catch (Exception e) {
            throw e;
        } finally {
            objTmp=null;
        }
        return chatProtocol;
    }
}
