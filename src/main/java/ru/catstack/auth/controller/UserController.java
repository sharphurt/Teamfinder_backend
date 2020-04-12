package ru.catstack.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.catstack.auth.dto.UserDto;
import ru.catstack.auth.model.User;
import ru.catstack.auth.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/users/")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id){
        Optional user = userService.findById(id);

        if(user.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserDto result = UserDto.fromUser((User) user.get());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
