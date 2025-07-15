package com.mouritech.security_frontend.controller;

import com.mouritech.security_frontend.dto.UserProfileDTO;
import com.mouritech.security_frontend.mainservice.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // ✅ Allow USER, SELLER, and ADMIN roles to fetch profile
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'SELLER', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(userProfileService.getProfile(user.getUsername()));
    }

    // ✅ Allow USER, SELLER, and ADMIN roles to update profile
    @PutMapping
    @PreAuthorize("hasAnyAuthority('USER', 'SELLER', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody UserProfileDTO dto) {
        return ResponseEntity.ok(userProfileService.updateProfile(user.getUsername(), dto));
    }
}
