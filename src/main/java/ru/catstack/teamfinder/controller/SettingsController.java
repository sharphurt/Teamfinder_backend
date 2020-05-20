package ru.catstack.teamfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;
import ru.catstack.teamfinder.service.SettingsService;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/api/admin/settings")
public class SettingsController {

    private final SettingsService settingsService;

    @Autowired
    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @PostMapping("jwt/expiration")
    public ApiResponse setJWTExpiration(@PathParam("dur") long dur) {
        settingsService.SetJWTExpiration(dur);
        return new ApiResponse("OK");
    }

    @PostMapping("jwt/prefix")
    public ApiResponse setJWTPrefix(@PathParam("prefix") String prefix) {
        settingsService.SetJWTPrefix(prefix);
        return new ApiResponse("OK");
    }

    @PostMapping("jwt/header")
    public ApiResponse setJWTHeader(@PathParam("header") String header) {
        settingsService.SetJWTHeaderName(header);
        return new ApiResponse("OK");
    }

    @PostMapping("jwt/secret")
    public ApiResponse setJWTSecret(@PathParam("secret") String secret) {
        settingsService.SetJWTHeaderName(secret);
        return new ApiResponse("OK");
    }
}
