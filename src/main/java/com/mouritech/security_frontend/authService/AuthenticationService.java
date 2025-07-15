package com.mouritech.security_frontend.authService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mouritech.security_frontend.dto.AuthenticationRequest;
import com.mouritech.security_frontend.dto.AuthenticationResponse;
import com.mouritech.security_frontend.dto.RegisterRequest;
import com.mouritech.security_frontend.entity.Role;
import com.mouritech.security_frontend.entity.User;
import com.mouritech.security_frontend.jwtservice.JwtService;
import com.mouritech.security_frontend.repository.UserRepository;

import java.util.HashMap;

@Service
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public AuthenticationResponse register(RegisterRequest registerRequest) {

		if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email already in use");
		}

		if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
			throw new IllegalArgumentException("Password and Confirm Password do not match");
		}

		User user = new User();
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setMobileNumber(registerRequest.getMobileNumber());

		user.setRole(registerRequest.getRole() != null ? registerRequest.getRole() : Role.USER);

		user.setCreatedBy("SYSTEM"); 
		user.setUpdatedBy("SYSTEM");

		userRepository.save(user);

		String jwtToken = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

		AuthenticationResponse response = new AuthenticationResponse();
		response.setAuthenticationToken(jwtToken);
		response.setRefreshToken(refreshToken);
		return response;
	}

	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
				authenticationRequest.getPassword()));

		User user = userRepository.findByEmail(authenticationRequest.getEmail())
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

		String jwtToken = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

		AuthenticationResponse response = new AuthenticationResponse();
		response.setAuthenticationToken(jwtToken);
		response.setRefreshToken(refreshToken);
		return response;
	}

	public AuthenticationResponse refreshToken(String refreshToken) {
		String email = jwtService.getEmailFromToken(refreshToken);

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

		System.out.println("EMAIL extracted from refresh token = " + email);

		String jwtToken = jwtService.generateToken(user);
		String newRefreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

		AuthenticationResponse response = new AuthenticationResponse();
		response.setAuthenticationToken(jwtToken);
		response.setRefreshToken(newRefreshToken);
		return response;
	}

	public Boolean validateToken(String token) {
		return jwtService.validateToken(token);
	}
}
