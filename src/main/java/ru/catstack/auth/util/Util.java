package ru.catstack.auth.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.model.Member;
import ru.catstack.auth.model.Team;
import ru.catstack.auth.repository.TeamRepository;

@Service
public class Util {
    private static TeamRepository teamRepository;

    @Autowired
    public Util(TeamRepository teamRepository) {
        Util.teamRepository = teamRepository;
    }

    public static boolean IsTeamContainsUser(Team team, long userId) {
        if (team.getCreator().getUser().getId().equals(userId))
            return true;
        for (var member : team.getMembers()) {
            if (member.getUser().getId().equals(userId))
                return true;
        }
        return false;
    }

    public static Team getTeamOrThrow(long teamId) {
        var optionalTeam = teamRepository.findById(teamId);
        if (optionalTeam.isEmpty())
            throw new ResourceNotFoundException("Team", "team id", teamId);
        return optionalTeam.get();
    }

}
