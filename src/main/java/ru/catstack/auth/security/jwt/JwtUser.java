package ru.catstack.auth.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.catstack.auth.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class JwtUser implements UserDetails {
    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final Byte age;
    private final String password;
    private final String email;
    private final boolean enabled;
    private final Instant lastPasswordResetDate;
    private final GrantedAuthority authorities;

    JwtUser(Long id, String username, String firstName, String lastName, Byte age, String email, String password,
            GrantedAuthority authority, boolean enabled, Instant lastPasswordResetDate) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.authorities = authority;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public User toUser() {
        return new User(this.id, this.email, this.password, this.username, this.firstName, this.lastName, this.age);
    }


    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authorities);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public Instant getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}
