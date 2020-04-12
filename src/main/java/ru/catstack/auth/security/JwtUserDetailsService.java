package ru.catstack.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.catstack.auth.model.User;
import ru.catstack.auth.repository.UserRepository;
import ru.catstack.auth.security.jwt.JwtUserFactory;
import ru.catstack.auth.service.UserService;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> dbUser = userRepository.findByUsername(username);
        logger.info("Fetched user : " + dbUser + " by " + username);
        return dbUser.map(JwtUserFactory::create)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user name in the database for " + username));
    }
}
