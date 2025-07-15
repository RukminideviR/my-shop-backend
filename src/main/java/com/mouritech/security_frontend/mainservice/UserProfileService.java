package com.mouritech.security_frontend.mainservice;


import com.mouritech.security_frontend.dto.UserProfileDTO;

public interface UserProfileService {
    UserProfileDTO getProfile(String email);
    UserProfileDTO updateProfile(String email, UserProfileDTO dto);
}
