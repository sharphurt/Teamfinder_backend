//package ru.catstack.auth.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.intellij.lang.annotations.RegExp;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import ru.catstack.auth.model.Role;
//import ru.catstack.auth.model.Status;
//import ru.catstack.auth.model.User;
//import ru.catstack.auth.repository.RoleRepository;
//import ru.catstack.auth.repository.UserRepository;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//
//@Service
//@Slf4j
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public User register(User user) throws Exception {
//        validateUser(user);
//        Role roleUser = roleRepository.findByName("ROLE_USER");
//        List<Role> userRoles = new ArrayList<>();
//        userRoles.add(roleUser);
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRoles(userRoles);
//        user.setStatus(Status.ACTIVE);
//        user.setCreated(new Date());
//        user.setUpdated(new Date());
//
//        return userRepository.save(user);
//    }
//
//    private void validateUser(User user) throws Exception {
//        if (userRepository.findByUsername(user.getUsername()) != null)
//            throw new Exception("User with that name is already exists");
//        if (userRepository.findByPassword(user.getPassword()) != null)
//            throw new Exception("User with that password is already exists");
//        if (userRepository.findByEmail(user.getEmail()) != null)
//            throw new Exception("User with that email is already exists");
//
//        if (user.getPassword().length() < 5)
//            throw new Exception("Too short password");
//
//        if (!user.getEmail().matches("\\A[a-z0-9!#$%&'*+/=?^_‘{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_‘{|}~-]+)*@\n(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\z"))
//            throw new Exception("Email is not valid");
//    }
//
//
//    @Override
//    public List<User> getAll() {
//        return userRepository.findAll();
//    }
//
//    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    @Override
//    public User findById(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    public void delete(Long id) {
//        userRepository.deleteById(id);
//    }
//}
