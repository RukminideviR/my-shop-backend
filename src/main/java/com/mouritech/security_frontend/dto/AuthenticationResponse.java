package com.mouritech.security_frontend.dto;

public class AuthenticationResponse {
    private String authenticationToken;
    private String refreshToken;
	public String getAuthenticationToken() {
		return authenticationToken;
	}
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public AuthenticationResponse(String authenticationToken, String refreshToken) {
		super();
		this.authenticationToken = authenticationToken;
		this.refreshToken = refreshToken;
	}
	public AuthenticationResponse() {
		super();
	}
    
    
}
