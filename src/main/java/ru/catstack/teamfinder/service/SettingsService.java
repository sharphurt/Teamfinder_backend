package ru.catstack.teamfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.config.SecurityConfig;
import ru.catstack.teamfinder.exception.AccessDeniedException;
import ru.catstack.teamfinder.model.RoleEnum;
import ru.catstack.teamfinder.model.Tag;
import ru.catstack.teamfinder.model.User;
import ru.catstack.teamfinder.repository.TagRepository;
import ru.catstack.teamfinder.repository.UserRepository;
import ru.catstack.teamfinder.security.jwt.JwtTokenProvider;

import java.util.List;
import java.util.Optional;

@Service
public class SettingsService {

    private final UserService userService;

    @Autowired
    public SettingsService(UserService userService) {
        this.userService = userService;
    }

    public void SetJWTExpiration(long duration) {
        var me = userService.getLoggedInUser();
        if (me.getRoles() != RoleEnum.ROLE_ADMIN)
            throw new AccessDeniedException("Only admins have access to this setting");
        JwtTokenProvider.validityInMilliseconds = duration;
    }

    public void SetJWtSecretWord(String word) {
        var me = userService.getLoggedInUser();
        if (me.getRoles() != RoleEnum.ROLE_ADMIN)
            throw new AccessDeniedException("Only admins have access to this setting");
        JwtTokenProvider.secret = word;
    }

    public void SetJWTHeaderName(String name) {
        var me = userService.getLoggedInUser();
        if (me.getRoles() != RoleEnum.ROLE_ADMIN)
            throw new AccessDeniedException("Only admins have access to this setting");
        JwtTokenProvider.tokenHeader = name;
    }

    public void SetJWTPrefix(String prefix) {
        var me = userService.getLoggedInUser();
        if (me.getRoles() != RoleEnum.ROLE_ADMIN)
            throw new AccessDeniedException("Only admins have access to this setting");
        JwtTokenProvider.tokenPrefix = prefix;
    }
}
