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

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final TeamService teamService;

    @Autowired
    public MessageService(MessageRepository messageRepository, TeamService teamService) {
        this.messageRepository = messageRepository;
        this.teamService = teamService;
    }

    public Message sendMessage(SendMessageRequest request) {
        var team = teamService.getTeamOrThrow(request.getTeamId());
        return createMessage(team, request.getMessage());
    }

    private Message createMessage(Team team, String message) {
        return teamService.getMemberByTeam(team)
                .map(member -> {
                    var msg = new Message(team, member, message);
                    return messageRepository.save(msg);
                })
                .orElseThrow(() -> new AccessDeniedException("You are not a member of this team."));
    }

    private Sort sort = new Sort(Sort.Direction.DESC, "createdAt");

    public List<Message> getMessagesGap(int from, int count, long teamId) {
        return messageRepository.findAllByTeamId(teamId, new OffsetBasedPage(from, count, sort)).getContent();
    }
}
