package ru.catstack.teamfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.exception.AccessDeniedException;
import ru.catstack.teamfinder.exception.ResourceAlreadyInUseException;
import ru.catstack.teamfinder.exception.ResourceNotFoundException;
import ru.catstack.teamfinder.model.*;
import ru.catstack.teamfinder.model.payload.request.TeamRegistrationRequest;
import ru.catstack.teamfinder.repository.TeamRepository;
import ru.catstack.teamfinder.util.OffsetBasedPage;
import ru.catstack.teamfinder.util.Util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final MemberService memberService;
    private final MessageService messageService;
    private final TagService tagService;

    @Autowired
    TeamService(TeamRepository teamRepository, UserService userService, MemberService memberService, MessageService messageService, TagService tagService) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.memberService = memberService;
        this.messageService = messageService;
        this.tagService = tagService;
    }

    public void registerTeam(TeamRegistrationRequest request) {
        var name = request.getName();
        var me = userService.getLoggedInUser();
        if (nameAlreadyExists(name))
            throw new ResourceAlreadyInUseException("Team name", "value", name);
        createTeam(me, request);
    }

    private boolean nameAlreadyExists(String name) {
        return teamRepository.existsByName(name);
    }

    private void createTeam(User creator, TeamRegistrationRequest request) {
        var creatorMember = memberService.createMember(creator, Set.of(new Role("CREATOR")));
        var team = new Team(request.getName(), request.getDescription(), creatorMember, request.getPicCode(), request.getTags());
        team.addMember(creatorMember);
        var saved = save(team);
        messageService.sendCreateMessage(saved, creatorMember);
    }

    public void deleteTeam(long teamId) {
        teamRepository.deleteById(teamId);
    }

    public void addMember(long userId, long teamId) {
        var member = memberService.createMember(userService.getUserOrThrow(userId), Set.of());
        var team = Util.getTeamOrThrow(teamId);
        addMember(member, team);
    }

    void addMember(Member member, Team team) {
        team.addMember(member);
        save(team);
        messageService.sendJoinMessage(team, member);
    }

    private void removeMember(Member member, Team team) {
        team.removeMember(member);
        save(team);
        messageService.sendLeaveMessage(team, member);
    }

    public void removeMember(long memberId, long teamId) {
        var member = getMemberOrThrow(memberId);
        var team = Util.getTeamOrThrow(teamId);
        removeMember(member, team);
    }

    private Member getMemberOrThrow(long memberId) {
        var optionalMember = memberService.findById(memberId);
        if (optionalMember.isEmpty())
            throw new ResourceNotFoundException("Member", "member id", memberId);
        return optionalMember.get();
    }

    public void addTag(long teamId, String tag) {
        var me = userService.getLoggedInUser();
        var team = Util.getTeamOrThrow(teamId);
        if (!team.getCreator().getUser().getId().equals(me.getId()))
            throw new AccessDeniedException("You do not have permission to edit tags list of this user");
        if (isTagAlreadyInUse(team, tag))
            throw new ResourceAlreadyInUseException("Tag", "name", tag);
        team.addTag(tag);
        teamRepository.save(team);
    }

    public void deleteTag(long tagId) {
        var me = userService.getLoggedInUser();
        tagService.findById(tagId).ifPresentOrElse(tag -> {
            var team = new ArrayList<>(tag.getTeams()).get(0);
            if (!team.getCreator().getUser().getId().equals(me.getId()))
                throw new AccessDeniedException("You do not have permission to edit tags list of this user");
            team.getTags().remove(tag);
            teamRepository.save(team);
        }, () -> {
            throw new ResourceNotFoundException("Tag", "role id", tagId);
        });
    }

    private boolean isTagAlreadyInUse(Team team, String tagName) {
        for (var tag : team.getTags()) {
            if (tag.getTag().equals(tagName))
                return true;
        }
        return false;
    }

    public void updateAvatarById(long teamId, String picCode) {
        var team = Util.getTeamOrThrow(teamId);
        var me = userService.getLoggedInUser();
        if (!team.getCreator().getUser().getId().equals(me.getId()))
            throw new AccessDeniedException("You cannot update avatar of this command");
        teamRepository.updateAvatarById(teamId, picCode);
        teamRepository.setUpdatedAtById(teamId, Instant.now());
    }

    public void updateNameById(long teamId, String name) {
        var team = Util.getTeamOrThrow(teamId);
        var me = userService.getLoggedInUser();
        if (!team.getCreator().getUser().getId().equals(me.getId()))
            throw new AccessDeniedException("You cannot update name of this command");
        teamRepository.updateNameById(teamId, name);
        teamRepository.setUpdatedAtById(teamId, Instant.now());
    }

    private long teamsCount() {
        return teamRepository.count();
    }

    public Optional<Team> getByTeamId(long teamId) {
        return teamRepository.findById(teamId);
    }

    private Team save(Team team) {
        return teamRepository.save(team);
    }

    private Sort sort = new Sort(Sort.Direction.DESC, "createdAt");

    public List<Team> getTeamsGap(int from, int count) {
        var teams = teamRepository.findAll(new OffsetBasedPage(from, count, sort)).getContent();
        return Util.fillApplicationStatusField(teams);
    }
}
