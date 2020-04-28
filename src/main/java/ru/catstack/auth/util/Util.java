package ru.catstack.auth.util;

import ru.catstack.auth.model.Member;
import ru.catstack.auth.model.Team;

public class Util {
    public static boolean IsTeamContainsUser(Team team, long userId) {
        if(team.getCreator().getUser().getId().equals(userId))
            return true;
        for (var member : team.getMembers()) {
            if (member.getId().equals(userId))
                return true;
        }
        return false;
    }
}
