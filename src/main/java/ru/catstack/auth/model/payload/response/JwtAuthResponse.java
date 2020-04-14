package ru.catstack.auth.model.payload.response;

public class JwtAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiryDuration;

    public JwtAuthResponse(String accessToken, String refreshToken, Long expiryDuration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryDuration = expiryDuration;
        tokenType = "Bearer ";
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpiryDuration() {
        return expiryDuration;
    }
}
