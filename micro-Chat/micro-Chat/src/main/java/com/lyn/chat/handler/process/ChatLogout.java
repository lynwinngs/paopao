package com.lyn.chat.handler.process;

import com.lyn.chat.protocal.ChatProtocol;
import com.lyn.chat.server.ChatUtils;

/**
 * 登出：
 * 获取发送者QQ号，移出在线列表。无需返回
 */
public class ChatLogout extends ChatProcess  {

    @Override
    public void run() {
        ChatProtocol protocol = getProtocol();
        if(protocol==null) return;
        String num = protocol.getSenderNum();
        ChatUtils.clientMap.remove(num);

        return;

    }
}
