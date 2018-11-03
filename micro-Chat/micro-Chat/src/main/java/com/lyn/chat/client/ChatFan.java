package com.lyn.chat.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatFan {
    private String fanNum;
    private HashMap<String,String> friend=new HashMap<>();


    public String getFanNum() {
        return fanNum;
    }

    public void setFanNum(String fanNum) {
        this.fanNum = fanNum;
    }

    public HashMap<String,String> getFriend() {
        return friend;
    }

    public void setFriend(HashMap<String, String> friend) {
        this.friend = friend;
    }

    public String getFriendList(){return null;}

}
