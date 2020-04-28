package ru.catstack.teamfinder.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.model.payload.request.SendMessageRequest;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.MessageService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/messages/")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ApiResponse registerTeam(@Valid @RequestBody SendMessageRequest request) {
        messageService.sendMessage(request);
        return new ApiResponse("Message send successfully");
    }

    @GetMapping("/get")
    public ApiResponse getTeams(@RequestParam int from, @RequestParam int count, @RequestParam long teamId) {
        var teams = messageService.getMessagesGap(from, count, teamId);
        return new ApiResponse(teams.toArray());
    }
}
