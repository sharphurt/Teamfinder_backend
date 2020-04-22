package ru.catstack.auth.controller;

import java.io.IOException;
import java.time.Instant;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.model.ImageModel;
import ru.catstack.auth.model.payload.request.ImageRequest;
import ru.catstack.auth.model.payload.response.ApiResponse;
import ru.catstack.auth.service.ImageService;
import ru.catstack.auth.service.UserService;

import javax.websocket.server.PathParam;

import static ru.catstack.auth.util.Util.*;

@RestController
@RequestMapping(value = "/api/image")
public class ImageController {

    private final ImageService imageService;
    private final UserService userService;

    public ImageController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @PostMapping("upload")
    public ApiResponse uploadImage(@RequestBody ImageRequest imageRequest) {
        var loggedUserId = userService.getLoggedInUser().get().getId();
        imageService.findByUserId(loggedUserId)
                .ifPresent(img -> imageService.deleteByUserId(loggedUserId));
        var imageName = (Instant.now() + "_thumbnail").replaceAll("(\\W)", "_");
        var image = new ImageModel(loggedUserId, imageName, imageRequest.getCode());
        imageService.save(image);
        userService.updateHasPictureById(loggedUserId, true);
        return new ApiResponse("Image was uploaded successfully");
    }

    @GetMapping("get")
    public ApiResponse getImage(@RequestParam Long id) {
        var requestedUser = userService.findById(id);
        return requestedUser.map(user -> {
            var retrievedImage = imageService.findByUserId(id);
            return retrievedImage.map(img -> {
                var responseImg = new ImageModel(id, img.getName(), img.getBase64Code());
                return new ApiResponse(responseImg);
            }).orElseThrow(() -> new ResourceNotFoundException("Avatar", "user id", id));
        }).orElseThrow(() -> new ResourceNotFoundException("Avatar", "user id", id));
    }

    @GetMapping("myImage")
    public ApiResponse getMyImage() {
        var me = userService.getLoggedInUser();
        return me.map(logged -> getImage(logged.getId())).orElseThrow();
    }


    @GetMapping("delete")
    public ApiResponse deleteImage() {
        var loggedUserId = userService.getLoggedInUser().get().getId();
        imageService.deleteByUserId(loggedUserId);
        userService.updateHasPictureById(loggedUserId, false);
        return new ApiResponse("Successfully deleted");
    }
}