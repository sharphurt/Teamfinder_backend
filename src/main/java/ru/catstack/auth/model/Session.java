package ru.catstack.auth.model;

import org.hibernate.annotations.Cascade;
import ru.catstack.auth.model.audit.DateAudit;
import ru.catstack.auth.model.payload.DeviceInfo;
import ru.catstack.auth.model.token.RefreshToken;

import javax.persistence.*;

@Entity(name = "session")
@Table(name = "session")
public class Session extends DateAudit {

    @Id
    @Column(name = "session_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "device_type")
    @Enumerated(value = EnumType.STRING)
    private DeviceType deviceType;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "is_refresh_active")
    private Boolean isRefreshActive;

    public Session() {
    }

    public Session(Long userId, DeviceInfo deviceInfo, Boolean isRefreshActive) {
        this.userId = userId;
        this.deviceType = deviceInfo.getDeviceType();
        this.deviceId = deviceInfo.getDeviceId();
        this.isRefreshActive = isRefreshActive;
    }

    public Long getId() {
        return id;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Boolean getRefreshActive() {
        return isRefreshActive;
    }

    public DeviceInfo toDeviceInfo() {
        return new DeviceInfo(this.deviceId, this.deviceType);
    }

    public Long getUserId() {
        return userId;
    }
}
