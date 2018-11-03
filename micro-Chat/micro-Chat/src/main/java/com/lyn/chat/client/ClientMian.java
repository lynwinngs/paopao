package com.lyn.chat.client;

/**
 * 客户端主类
 * 启动客户端接收线程和发送线程；
 * 使用线程池处理
 */
public class ClientMian {
    public static void main(String[] args) {
        ChatClientAccept chatClientAccept = null;
        ChatClient chatClient = null;
        ChatFan fan = null;
        try {
            fan = new ChatFan();
            chatClientAccept = new ChatClientAccept(fan);
            if (chatClientAccept.getPort()==0 ){
                throw new Exception("接收socket未初始化");
            }
            new Thread(chatClientAccept).start();

            fan.setFanNum(String.valueOf((int)(Math.random()*1000000)));
            chatClient = new ChatClient(chatClientAccept.getPort(),fan);
            new Thread(chatClient).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
