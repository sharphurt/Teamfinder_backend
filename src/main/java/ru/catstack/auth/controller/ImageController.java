package ru.catstack.auth.controller;

import java.io.IOException;
import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.catstack.auth.exception.ResourceNotFoundException;
import ru.catstack.auth.model.ImageModel;
import ru.catstack.auth.model.payload.response.ApiResponse;
import ru.catstack.auth.service.ImageService;
import ru.catstack.auth.service.UserService;

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
    public ApiResponse uploadImage(@RequestParam("imageFile") MultipartFile file) throws IOException {
        var loggedUserId = userService.getLoggedInUser().get().getId();
        imageService.findByUserId(loggedUserId)
                .ifPresent(img -> imageService.deleteByUserId(loggedUserId));
        var image = new ImageModel(loggedUserId, Instant.now() + "_thumbnail",
                file.getContentType(), compressBytes(file.getBytes()));
        imageService.save(image);
        userService.updateHasPictureById(loggedUserId, true);
        return new ApiResponse("Image was uploaded successfully");
    }

    @GetMapping("get")
    public ApiResponse getImage() {
        var loggedUserId = userService.getLoggedInUser().get().getId();
        var retrievedImage = imageService.findByUserId(loggedUserId);
        return retrievedImage.map(img -> {
            var responseImg = new ImageModel(loggedUserId, img.getName(), img.getType(), decompressBytes(img.getPicBytes()));
            return new ApiResponse(responseImg);
        }).orElseThrow(() -> new ResourceNotFoundException("Image", "user id", loggedUserId));
    }

    @GetMapping("delete")
    public ApiResponse deleteImage() {
        var loggedUserId = userService.getLoggedInUser().get().getId();
        imageService.deleteByUserId(loggedUserId);
        userService.updateHasPictureById(loggedUserId, false);
        return new ApiResponse("Successfully deleted");
    }
}