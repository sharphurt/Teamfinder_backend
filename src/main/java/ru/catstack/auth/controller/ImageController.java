package ru.catstack.auth.controller;

import org.springframework.web.bind.annotation.*;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.model.Avatar;
import ru.catstack.auth.model.payload.request.ImageRequest;
import ru.catstack.auth.model.payload.response.ApiResponse;
import ru.catstack.auth.service.UserService;

@RestController
@RequestMapping(value = "/api/image")
public class ImageController {

    private final UserService userService;

    public ImageController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("upload")
    public ApiResponse uploadImage(@RequestBody ImageRequest imageRequest) {
        var me = userService.getLoggedInUser();
        userService.updateAvatarById(me.getId(), imageRequest.getCode());
        return new ApiResponse("Image was uploaded successfully");
    }

    @GetMapping("get")
    public ApiResponse getImage(@RequestParam Long id) {
        var requestedUser = userService.findById(id);
        return requestedUser
                .map(user -> new ApiResponse(Avatar.fromUser(user)))
                .orElseThrow(() -> new ResourceNotFoundException("Avatar", "user id", id));
    }

    @GetMapping("my")
    public ApiResponse getMyImage() {
        var me = userService.getLoggedInUser();
        return getImage(me.getId());
    }


    @GetMapping("delete")
    public ApiResponse deleteImage() {
        var me = userService.getLoggedInUser().getId();
        userService.updateAvatarById(me, null);
        return new ApiResponse("Successfully deleted");
    }
}