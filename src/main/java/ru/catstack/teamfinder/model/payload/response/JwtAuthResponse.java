package ru.catstack.teamfinder.model.payload.response;

public class JwtAuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long accessTokenExpiryDur;

    public JwtAuthResponse(String accessToken, String refreshToken, Long accessTokenExpiryDur, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiryDur = accessTokenExpiryDur;
        this.tokenType = tokenType;
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

    public Long getAccessTokenExpiryDur() {
        return accessTokenExpiryDur;
    }
}
