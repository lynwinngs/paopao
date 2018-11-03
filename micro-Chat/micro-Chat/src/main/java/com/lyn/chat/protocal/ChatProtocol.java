package com.lyn.chat.protocal;

import java.io.Serializable;

public class ChatProtocol implements Serializable {

    private String senderIP;
    private int senderPort;
    private String recipientIP;
    private int recipientPort;
    private String code ;
    private String senderNum;
    private String recipientNum;
    private String message;

    @Override
    public String toString(){
        return senderIP       + ", " +
                senderPort     + ", " +
                recipientIP    + ", " +
                recipientPort  + ", " +
                code           + ", " +
                senderNum      + ", " +
                recipientNum   + ", " +
                "message: "+message ;
    }

    public String getRecipientIP() {
        return recipientIP;
    }

    public void setRecipientIP(String recipientIP) {
        this.recipientIP = recipientIP;
    }

    public int getRecipientPort() {
        return recipientPort;
    }

    public void setRecipientPort(int recipientPort) {
        this.recipientPort = recipientPort;
    }

    public ChatProtocol(String senderIP, int senderPort, String recipientIP,
                        int recipientPort, String code, String senderNum,
                        String recipientNum, String message) {
        this.senderIP = senderIP;
        this.senderPort = senderPort;
        this.recipientIP = recipientIP;
        this.recipientPort = recipientPort;
        this.code = code;
        this.senderNum = senderNum;
        this.recipientNum = recipientNum;
        this.message = message;
    }

    public String getSenderIP() {
        return senderIP;
    }

    public void setSenderIP(String senderIP) {
        this.senderIP = senderIP;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSenderNum() {
        return senderNum;
    }

    public void setSenderNum(String senderNum) {
        this.senderNum = senderNum;
    }

    public String getRecipientNum() {
        return recipientNum;
    }

    public void setRecipientNum(String recipientNum) {
        this.recipientNum = recipientNum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
