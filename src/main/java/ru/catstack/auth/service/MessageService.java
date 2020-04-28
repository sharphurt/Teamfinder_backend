package ru.catstack.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.AccessDeniedException;
import ru.catstack.auth.model.Member;
import ru.catstack.auth.model.Message;
import ru.catstack.auth.model.MessageType;
import ru.catstack.auth.model.Team;
import ru.catstack.auth.model.payload.request.SendMessageRequest;
import ru.catstack.auth.repository.MessageRepository;
import ru.catstack.auth.util.OffsetBasedPage;
import ru.catstack.auth.util.Util;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
 //   private final TeamService teamService;
    private final UserService userService;
    private final MemberService memberService;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserService userService, MemberService memberService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.memberService = memberService;
    }

    public void sendMessage(SendMessageRequest request) {
        var me = userService.getLoggedInUser();
        var team = Util.getTeamOrThrow(request.getTeamId());
        if (!Util.IsTeamContainsUser(team, me.getId()))
            throw new AccessDeniedException("You do not have access to the messages of this command");
        var member = memberService.getMemberByTeam(team).get();
        createMessage(team, member, MessageType.ORDINARY, request.getMessage());
    }

    private void createMessage(Team team, Member sender, MessageType type, String message) {
        var msg = new Message(team, sender, type, message);
        messageRepository.save(msg);
    }

    void sendJoinMessage(Team team, Member member) {
        createMessage(team, member, MessageType.JOIN_TEAM, "User " + member.getUser().getUsername() + " joined the team");
    }

    void sendLeaveMessage(Team team, Member member) {
        createMessage(team, member, MessageType.LEAVE_TEAM, "User " + member.getUser().getUsername() + " leaved the team");
    }

    void sendCreateMessage(Team team, Member member) {
        createMessage(team, member, MessageType.CREATE_TEAM, "User " + member.getUser().getUsername() + " created the team");
    }

    private Sort sort = new Sort(Sort.Direction.DESC, "createdAt");

    public List<Message> getMessagesGap(int from, int count, long teamId) {
        var team = Util.getTeamOrThrow(teamId);
        var me = userService.getLoggedInUser();
        if (!Util.IsTeamContainsUser(team, me.getId()))
            throw new AccessDeniedException("You do not have access to the messages of this command");
        return messageRepository.findAllByTeamId(teamId, new OffsetBasedPage(from, count, sort)).getContent();
    }
}
