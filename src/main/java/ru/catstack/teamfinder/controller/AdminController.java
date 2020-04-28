package ru.catstack.teamfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.catstack.teamfinder.exception.NoContentException;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.UserService;

@RestController
@RequestMapping(value = "/api/admin/")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "users/{id}")
    public ApiResponse getUserById(@PathVariable(name = "id") Long id) {
        var user = userService.findById(id);
        return user.map(ApiResponse::new).orElseThrow(() -> new NoContentException("User", "id", id));
    }
}
