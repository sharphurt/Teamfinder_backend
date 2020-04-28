package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.AccessDeniedException;
import ru.catstack.auth.model.Message;
import ru.catstack.auth.model.Team;
import ru.catstack.auth.model.payload.request.SendMessageRequest;
import ru.catstack.auth.repository.MessageRepository;
import ru.catstack.auth.util.OffsetBasedPage;
import ru.catstack.auth.util.Util;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final TeamService teamService;
    private final UserService userService;
    private final MemberService memberService;

    @Autowired
    public MessageService(MessageRepository messageRepository, TeamService teamService, UserService userService, MemberService memberService) {
        this.messageRepository = messageRepository;
        this.teamService = teamService;
        this.userService = userService;
        this.memberService = memberService;
    }

    public void sendMessage(SendMessageRequest request) {
        var me = userService.getLoggedInUser();
        var team = teamService.getTeamOrThrow(request.getTeamId());
        if (!Util.IsTeamContainsUser(team, me.getId()))
            throw new AccessDeniedException("You do not have access to the messages of this command");
        createMessage(team, request.getMessage());
    }

    private void createMessage(Team team, String message) {
        memberService.getMemberByTeam(team)
                .map(member -> {
                    var msg = new Message(team, member, message);
                    return messageRepository.save(msg);
                })
                .orElseThrow(() -> new AccessDeniedException("You are not a member of this team."));
    }

    private Sort sort = new Sort(Sort.Direction.DESC, "createdAt");

    public List<Message> getMessagesGap(int from, int count, long teamId) {
        var team = teamService.getTeamOrThrow(teamId);
        var me = userService.getLoggedInUser();
        if (!Util.IsTeamContainsUser(team, me.getId()))
            throw new AccessDeniedException("You do not have access to the messages of this command");
        return messageRepository.findAllByTeamId(teamId, new OffsetBasedPage(from, count, sort)).getContent();
    }
}
