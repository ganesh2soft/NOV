package com.hcl.springecomapp.payload;



import org.springframework.http.ResponseCookie;

public class AuthenticationResult {

    private ResponseCookie jwtCookie;
    private UserInfoResponse response;

    public AuthenticationResult() {}

    public AuthenticationResult(ResponseCookie jwtCookie, UserInfoResponse response) {
        this.jwtCookie = jwtCookie;
        this.response = response;
    }

    public ResponseCookie getJwtCookie() {
        return jwtCookie;
    }

    public void setJwtCookie(ResponseCookie jwtCookie) {
        this.jwtCookie = jwtCookie;
    }

    public UserInfoResponse getResponse() {
        return response;
    }

    public void setResponse(UserInfoResponse response) {
        this.response = response;
    }
}
