package ru.catstack.auth.model.payload;

import ru.catstack.auth.model.DeviceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DeviceInfo {
    @NotBlank(message = "Device id cannot be blank")
    @NotNull(message = "Device id cannot be null")
    private String deviceId;

    @NotNull(message = "Device type cannot be null")
    private DeviceType deviceType;

    public DeviceInfo() {
    }

    public DeviceInfo(String deviceId, DeviceType type) {
        this.deviceId = deviceId;
        deviceType = type;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
}
