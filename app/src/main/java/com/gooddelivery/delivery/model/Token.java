package com.gooddelivery.delivery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamil on 9/22/2017.
 */

public class Token {


    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Object getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
