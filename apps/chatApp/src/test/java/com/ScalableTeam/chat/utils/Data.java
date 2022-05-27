package com.ScalableTeam.chat.utils;

import com.ScalableTeam.chat.app.entity.GroupChat;
import com.ScalableTeam.chat.app.entity.Message;
import com.ScalableTeam.chat.app.entity.PrivateChat;
import com.google.cloud.Timestamp;

import java.util.Arrays;

public class Data {

    public static final PrivateChat pvChat = new PrivateChat(Arrays.asList("1", "2"));

    public static final GroupChat grpChat = new GroupChat("FAM_GRP", "DESCRIPTION", "1", Arrays.asList("1", "2"));

    public static final Message m1 = new Message("1", "content1", Timestamp.now());

    public static final Message m2 = new Message("1", "content2", Timestamp.now());



}
