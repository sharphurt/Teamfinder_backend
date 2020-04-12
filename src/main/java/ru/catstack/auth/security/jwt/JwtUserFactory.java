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
                user.getPassword(), mapToGrantedAuthorities(new ArrayList<>(user.getRoles())),
                user.getStatus().equals(Status.ACTIVE), user.getUpdatedAt()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getRole().name())
                ).collect(Collectors.toList());
    }
}
