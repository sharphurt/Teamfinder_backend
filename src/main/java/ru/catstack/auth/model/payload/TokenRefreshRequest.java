package ru.catstack.auth.model.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TokenRefreshRequest {

    @NotBlank(message = "Refresh token cannot be blank")
    private String refreshToken;

    @NotNull(message = "Device info cannot be null")
    private DeviceInfo deviceInfo;

    public TokenRefreshRequest(String refreshToken, DeviceInfo deviceInfo) {
        this.refreshToken = refreshToken;
        this.deviceInfo = deviceInfo;
    }

    public TokenRefreshRequest() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }
}
