package com.ScalableTeam.chat.app.GroupChat;

import com.ScalableTeam.chat.app.entity.GroupChat;
import com.ScalableTeam.chat.app.entity.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/chat/group-chat")
public class GroupChatController {

    private GroupChatService groupChatService;

    public GroupChatController(GroupChatService groupChatService) {
        this.groupChatService = groupChatService;
    }

    @PostMapping("/createGroup")
    public String createGroup(@RequestBody Map<String, Object> requestBody) {
        return groupChatService.createGroup(requestBody);

    }

    @PutMapping("/updateGroup")
    public String updateGroup(@RequestBody Map<String, Object> requestBody) {
        return groupChatService.updateGroup(requestBody);

    }

    @DeleteMapping("/deleteGroup/{groupChatId}")
    public String deleteGroup(@PathVariable String groupChatId) {
        return groupChatService.deleteGroup(groupChatId);

    }

    @PostMapping("/addMember")
    public String addMember(@RequestBody Map<String, String> requestBody) {
        return groupChatService.addMember(requestBody);

    }

    @PostMapping("/removeMember")
    public String removeMember(@RequestBody Map<String, String> requestBody) {
        return groupChatService.removeMember(requestBody);
    }

    @PostMapping("/sendMessage")
    public String sendMessageToGroup(@RequestBody Map<String, String> requestBody) {
        return groupChatService.sendMessageToGroup(requestBody);
    }


    @GetMapping(path = "/getMessages/{groupChatId}")
    public List<Message> getGroupChat(@PathVariable String groupChatId, @RequestParam("lastMessageId") String lastMessageId) {
        return groupChatService.getMessagesByGroupId(groupChatId, lastMessageId);
    }

}
