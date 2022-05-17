package com.ScalableTeam.chat.app.privateChat;

import com.ScalableTeam.chat.app.entity.Message;
import com.ScalableTeam.chat.app.entity.PrivateChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/chat/private-chat")
public class PrivateChatController {

    public PrivateChatService privateChatService;

    @Autowired
    public PrivateChatController(PrivateChatService privateChatService) {
        this.privateChatService = privateChatService;
    }

    @PostMapping(path = "/createChat")
    public String createChat(@RequestBody Map<String, List<String>> users) {

        return privateChatService.createChat(users);
    }

    @PostMapping(path = "/addMessage")
    public String addMessage(@RequestBody Map<String, String> newMessage) {

        return privateChatService.addMessage(newMessage);
    }

    @GetMapping(path = "/getMessages/{privateChatId}")
    public List<Message> getChat(@PathVariable String privateChatId, @RequestParam("lastMessageId") String lastMessageId) {
        return privateChatService.getChat(privateChatId, lastMessageId);
    }

    @GetMapping(path = "/getAllChats/{userId}")
    public List<PrivateChat> getAllChats(@PathVariable String userId) {

        return privateChatService.getAllChats(userId);

    }
}
