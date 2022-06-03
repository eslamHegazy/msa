package com.ScalableTeam.chat;

import com.ScalableTeam.chat.app.GroupChat.*;
import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.PrivateChat.AddMessageCommand;
import com.ScalableTeam.chat.app.PrivateChat.CreateChatCommand;
import com.ScalableTeam.chat.app.PrivateChat.GetAllChatsCommand;
import com.ScalableTeam.chat.app.PrivateChat.GetChatCommand;
import com.ScalableTeam.chat.app.entity.GroupChat;
import com.ScalableTeam.chat.app.entity.Message;
import com.ScalableTeam.chat.app.entity.PrivateChat;
import com.ScalableTeam.chat.utils.DBMethods;
import com.ScalableTeam.chat.utils.DataPopulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.common.truth.Truth.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ChatApplication.class)
public class ChatApplicationTest {

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void prepareData() throws ExecutionException, InterruptedException {
        DataPopulation.clearDB();
    }

//    @AfterEach
//    public void clearData() throws ExecutionException, InterruptedException {
//
//    }

    @Test
    public void createPrivateChat() throws ExecutionException, InterruptedException {

        Map<String, Object> data = new HashMap<>();
        data.put("users", Arrays.asList("3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateChatCommand.class);
        String id = (String) cmd.execute(data);

        Object doc = DBMethods.checkDocExists("PrivateChats", id);
        assertThat(doc).isNotNull();
    }

    @Test
    public void addMessageToPrivateChat() throws ExecutionException, InterruptedException {
        // Add data to collection of chats
        Map<String, Object> data = new HashMap<>();
        data.put("users", Arrays.asList("3", "4"));

        MyCommand cmd = context.getBean(CreateChatCommand.class);
        String chatId = (String) cmd.execute(data);

        // Add Messages
        Map<String, Object> msgData = new HashMap<>();
        msgData.put("authorId", "1");
        msgData.put("content", "TESSTTTT");
        msgData.put("privateChatId", chatId);

        // Add data to collection of chats
        MyCommand cmd2 = context.getBean(AddMessageCommand.class);
        String msgId = (String) cmd2.execute(msgData);
        Object doc = DBMethods.checkMsgDocExists("PrivateChats", chatId, msgId);
        assertThat(doc).isNotNull();
    }

    @Test
    public void getMessagesFromPrivateChat() throws ExecutionException, InterruptedException {
        // Add data to collection of chats
        Map<String, Object> data = new HashMap<>();
        data.put("users", Arrays.asList("1", "4"));

        MyCommand cmd = context.getBean(CreateChatCommand.class);
        String id = (String) cmd.execute(data);

        // Add Messages
        Map<String, Object> msgData = new HashMap<>();
        msgData.put("authorId", "1");
        msgData.put("content", "TESSTTTT");
        msgData.put("privateChatId", id);

        // Add data to collection of chats
        MyCommand cmd2 = context.getBean(AddMessageCommand.class);
        String msgId = (String) cmd2.execute(msgData);
        String msgId2 = (String) cmd2.execute(msgData);

        // Get Messages by chat id
        Map<String, Object> getMap = new HashMap<>();
        getMap.put("chatId", id);
        getMap.put("lastMessageId", msgId2);

        MyCommand getMsgsCmd = context.getBean(GetChatCommand.class);
        List<Message> msgs = (List<Message>) getMsgsCmd.execute(getMap);
        assertThat(msgs).hasSize(1);
    }

    @Test
    public void createGroupChat() throws ExecutionException, InterruptedException {

        Map<String, Object> data = new HashMap<>();
        data.put("adminId", "1");
        data.put("name", "NEWGRP");
        data.put("description", "GRP DESCC");
        data.put("users", Arrays.asList("3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateGroupCommand.class);
        String id = (String) cmd.execute(data);
        Object doc = DBMethods.checkDocExists("GroupChats", id);
        assertThat(doc).isNotNull();
    }

    @Test
    public void deleteGroupChat() throws ExecutionException, InterruptedException {

        Map<String, Object> data = new HashMap<>();
        data.put("adminId", "1");
        data.put("name", "NEWGRP");
        data.put("description", "GRP DESCC");
        data.put("users", Arrays.asList("3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateGroupCommand.class);
        String id = (String) cmd.execute(data);

        // Delete Chat
        Map<String, Object> delData = new HashMap<>();
        delData.put("groupChatId", id);
        MyCommand cmd2 = context.getBean(DeleteGroupCommand.class);
        cmd2.execute(delData);

        Object doc = DBMethods.checkDocExists("GroupChats", id);
        assertThat(doc).isNull();
    }

    @Test
    public void updateGroupChat() throws ExecutionException, InterruptedException {

        Map<String, Object> data = new HashMap<>();
        data.put("adminId", "1");
        data.put("name", "NEWGRP");
        data.put("description", "GRP DESCC");
        data.put("users", Arrays.asList("3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateGroupCommand.class);
        String id = (String) cmd.execute(data);

        // Update Chat
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("id", id);
        updateData.put("name", "Updated Name");
        updateData.put("description", "Updated DESC");

        MyCommand cmd2 = context.getBean(UpdateGroupCommand.class);
        Map<String, Object> updatedDoc = (Map<String, Object>) cmd2.execute(updateData);

        assertThat(updatedDoc.get("name")).isEqualTo("Updated Name");
        assertThat(updatedDoc.get("description")).isEqualTo("Updated DESC");
    }

    @Test
    public void addMemberGroupChat() throws ExecutionException, InterruptedException {

        Map<String, Object> data = new HashMap<>();
        data.put("adminId", "1");
        data.put("name", "NEWGRP");
        data.put("description", "GRP DESCC");
        data.put("users", Arrays.asList("3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateGroupCommand.class);
        String id = (String) cmd.execute(data);

        // Add Member Chat
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("groupId", id);
        updateData.put("memberId", "12");

        MyCommand cmd2 = context.getBean(AddMemberCommand.class);
        Map<String, Object> updatedDoc = (Map<String, Object>) cmd2.execute(updateData);

        List<String> members = (List<String>) updatedDoc.get("users");
        assertThat(members).contains("12");
    }

    @Test
    public void removeMemberGroupChat() throws ExecutionException, InterruptedException {

        Map<String, Object> data = new HashMap<>();
        data.put("adminId", "1");
        data.put("name", "NEWGRP");
        data.put("description", "GRP DESCC");
        data.put("users", Arrays.asList("3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateGroupCommand.class);
        String id = (String) cmd.execute(data);

        // Add Member Chat
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("groupId", id);
        updateData.put("memberId", "3");

        MyCommand cmd2 = context.getBean(RemoveMemberCommand.class);
        Map<String, Object> updatedDoc = (Map<String, Object>) cmd2.execute(updateData);

        List<String> members = (List<String>) updatedDoc.get("users");
        assertThat(members).doesNotContain("3");
    }

    @Test
    public void sendMessageToGroupChat() throws ExecutionException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put("adminId", "1");
        data.put("name", "NEWGRP");
        data.put("description", "GRP DESCC");
        data.put("users", Arrays.asList("3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateGroupCommand.class);
        String grpId = (String) cmd.execute(data);

        Map<String, Object> msgData = new HashMap<>();
        msgData.put("authorId", "1");
        msgData.put("content", "TESSTTTT");
        msgData.put("groupChatId", grpId);

        // Add data to collection of chats
        MyCommand cmd2 = context.getBean(SendMessageToGroupCommand.class);
        String msgId = (String) cmd2.execute(msgData);
        Object doc = DBMethods.checkMsgDocExists("GroupChats", grpId, msgId);
        assertThat(doc).isNotNull();
    }

    @Test
    public void getMessagesFromGroupChat() throws ExecutionException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put("adminId", "1");
        data.put("name", "NEWGRP");
        data.put("description", "GRP DESCC");
        data.put("users", Arrays.asList("3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateGroupCommand.class);
        String grpId = (String) cmd.execute(data);

        Map<String, Object> msgData = new HashMap<>();
        msgData.put("authorId", "1");
        msgData.put("content", "TESSTTTT");
        msgData.put("groupChatId", grpId);

        // Add data to collection of chats
        MyCommand cmd2 = context.getBean(SendMessageToGroupCommand.class);
        String msgId = (String) cmd2.execute(msgData);
        String msgId2 = (String) cmd2.execute(msgData);

        // Get Messages by chat id
        Map<String, Object> getMap = new HashMap<>();
        getMap.put("chatId", grpId);
        getMap.put("lastMessageId", msgId2);
        getMap.put("groupChatId", grpId);

        MyCommand getMsgsCmd = context.getBean(GetMessageByGroupIdCommand.class);
        List<Message> msgs = (List<Message>) getMsgsCmd.execute(getMap);
        assertThat(msgs).hasSize(1);
    }

    @Test
    public void getAllChats() throws ExecutionException, InterruptedException {
        // Group Chat including user 1
        Map<String, Object> grpData = new HashMap<>();
        grpData.put("adminId", "1");
        grpData.put("name", "NEWGRP");
        grpData.put("description", "GRP DESCC");
        grpData.put("users", Arrays.asList("1", "3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateGroupCommand.class);
        String grpId = (String) cmd.execute(grpData);

        // Add Private CHat
        Map<String, Object> pvData = new HashMap<>();
        pvData.put("users", Arrays.asList("1", "4"));

        // Add data to collection of chats
        MyCommand cmd2 = context.getBean(CreateChatCommand.class);
        String pvtId = (String) cmd2.execute(pvData);

        // Get ALl chats
        // Add Private CHat
        Map<String, Object> getData = new HashMap<>();
        getData.put("userId", "1");

        MyCommand getCmd = context.getBean(GetAllChatsCommand.class);
        Map<String, Object> allChats = (Map<String, Object>) getCmd.execute(getData);

        List<PrivateChat> pvChats = (List<PrivateChat>) allChats.get("PrivateChats");
        List<GroupChat> grpChats = (List<GroupChat>) allChats.get("GroupChats");

        assertThat(pvChats).hasSize(1);
        assertThat(grpChats).hasSize(1);

        List<String> pvUsers = pvChats.get(0).getUsers();
        List<String> grpUsers = grpChats.get(0).getUsers();
        assertThat(pvUsers).contains("1");
        assertThat(grpUsers).contains("1");

    }
}
