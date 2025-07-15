package com.mouritech.security_frontend.controller;

import com.mouritech.security_frontend.authService.AuthenticationService;
import com.mouritech.security_frontend.dto.AuthenticationRequest;
import com.mouritech.security_frontend.dto.AuthenticationResponse;
import com.mouritech.security_frontend.dto.RegisterRequest;
import com.mouritech.security_frontend.repository.PasswordResetTokenRepository;
import com.mouritech.security_frontend.repository.UserRepository;

import jakarta.validation.Valid;

import com.mouritech.security_frontend.entity.User;
import com.mouritech.security_frontend.jwtservice.EmailService;
import com.mouritech.security_frontend.jwtservice.OtpStore;
import com.mouritech.security_frontend.mainentity.PasswordResetToken;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth-service")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final OtpStore otpStore;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(
            AuthenticationService authenticationService,
            EmailService emailService,
            OtpStore otpStore,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
           PasswordResetTokenRepository tokenRepository) {
        this.authenticationService = authenticationService;
        this.emailService = emailService;
        this.otpStore = otpStore;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository= tokenRepository;
    }

    // -------------------- AUTH --------------------

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest)
 {
        try {
            AuthenticationResponse res = authenticationService.register(registerRequest);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            AuthenticationResponse res = authenticationService.authenticate(authenticationRequest);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestParam("token") String refreshToken) {
        try {
            AuthenticationResponse res = authenticationService.refreshToken(refreshToken);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        try {
            Boolean res = authenticationService.validateToken(token);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusMinutes(10));
            tokenRepository.save(resetToken);

            
            return ResponseEntity.ok("Reset token (valid 10 min): " + token);
        } else {
            return ResponseEntity.ok("If user exists, reset token was generated.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String newPassword = payload.get("newPassword");

        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isPresent()) {
            PasswordResetToken resetToken = tokenOpt.get();
            if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("Token expired.");
            }

            User user = resetToken.getUser();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            tokenRepository.delete(resetToken);
            return ResponseEntity.ok("Password reset successful.");
        } else {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
    }

    
}
