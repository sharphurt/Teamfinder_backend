package ru.catstack.teamfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.catstack.teamfinder.exception.ResourceAlreadyInUseException;
import ru.catstack.teamfinder.exception.ResourceNotFoundException;
import ru.catstack.teamfinder.model.RoleEnum;
import ru.catstack.teamfinder.model.Status;
import ru.catstack.teamfinder.model.User;
import ru.catstack.teamfinder.model.payload.request.RegistrationRequest;
import ru.catstack.teamfinder.repository.UserRepository;
import ru.catstack.teamfinder.security.jwt.JwtTokenProvider;
import ru.catstack.teamfinder.security.jwt.JwtUser;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long Id) {
        return userRepository.findById(Id);
    }

    User save(User user) {
        return userRepository.save(user);
    }

    Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void updateAvatarById(Long id, String avatarCode) {
        userRepository.updateAvatarById(id, avatarCode);
        setUpdatedAtById(id, Instant.now());
    }

    User getUserOrThrow(long userId) {
        var optionalUser = findById(userId);
        if (optionalUser.isEmpty())
            throw new ResourceNotFoundException("Member", "member id", userId);
        return optionalUser.get();
    }

    User createUser(RegistrationRequest registerRequest) {
        return new User(registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getUsername(), registerRequest.getFirstName(), registerRequest.getLastName(),
                registerRequest.getAge(), RoleEnum.ROLE_USER, Status.ACTIVE);
    }

    public Long getUserIdFromRequest(HttpServletRequest request) {
        var token = Optional.of(jwtTokenProvider.resolveToken(request));
        return token.map(jwtTokenProvider::getUserId).orElseThrow(() -> new AccessDeniedException("Access denied"));
    }

    public User getLoggedInUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken)
            throw new AccessDeniedException("Unexpected error");
        return findById(((JwtUser) auth.getPrincipal()).getId())
                .orElseThrow(() -> new AccessDeniedException("An unexpected error occurred while trying to get user data"));
    }


    public void updateUsernameById(Long id, String username) {
        if (existsByUsername(username))
            throw new ResourceAlreadyInUseException("Username", "value", username);
        userRepository.updateUsernameById(id, username);
        setUpdatedAtById(id, Instant.now());
    }

    public void updateFirstNameById(Long id, String firstName) {
        userRepository.updateFirstNameById(id, firstName);
        setUpdatedAtById(id, Instant.now());
    }

    public void updateLastNameById(Long id, String lastName) {
        userRepository.updateLastNameById(id, lastName);
        setUpdatedAtById(id, Instant.now());
    }

    public void updateAgeById(Long id, Byte age) {
        userRepository.updateAgeById(id, age);
        setUpdatedAtById(id, Instant.now());
    }

    public void updateEmailById(Long id, String email) {
        if (existsByEmail(email))
            throw new ResourceAlreadyInUseException("Email", "value", email);
        userRepository.updateEmailById(id, email);
        setUpdatedAtById(id, Instant.now());
    }

    public void updatePasswordById(Long id, String password) {
        userRepository.updatePasswordById(id, passwordEncoder.encode(password));
        setUpdatedAtById(id, Instant.now());
    }

    public void updateAboutMeById(Long id, String aboutMe) {
        userRepository.updateAboutMeById(id, aboutMe);
        setUpdatedAtById(id, Instant.now());
    }

    private void setUpdatedAtById(Long id, Instant updatedAt) {
        userRepository.setUpdatedAtById(id, updatedAt);
    }
}
