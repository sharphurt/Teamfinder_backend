package ru.catstack.auth.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.catstack.auth.model.Role;
import ru.catstack.auth.model.Status;
import ru.catstack.auth.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(), user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getAge(), user.getEmail(),
                user.getPassword(), mapToGrantedAuthorities(user.getRoles()),
                user.getStatus().equals(Status.ACTIVE), user.getUpdatedAt()
        );
    }

    private static GrantedAuthority mapToGrantedAuthorities(String userRoles) {
        return new SimpleGrantedAuthority(userRoles);
    }
}
