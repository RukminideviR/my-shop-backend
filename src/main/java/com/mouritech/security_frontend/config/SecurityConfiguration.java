package com.mouritech.security_frontend.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.mouritech.security_frontend.authService.JwtAuthenticationFilter;
import com.mouritech.security_frontend.entity.Role;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;

	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.authenticationProvider = authenticationProvider;
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(List.of("http://localhost:3000")); // frontend origin
		config.setAllowedHeaders(List.of("Origin", "Content-Type", "Accept", "Authorization"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source); // ✅ DO NOT remove this
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors() // ✅ enable CORS
				.and().csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(request -> request.requestMatchers("/api/auth-service/forgot-password")
						.permitAll().requestMatchers("/api/auth-service/reset-password").permitAll()
						.requestMatchers("/api/v1/auth-service/**", "/v3/api-docs/**", "/swagger-ui/**",
								"/swagger-ui.html", "/webjars/**")
						.permitAll()

						// PUBLIC access
						.requestMatchers(HttpMethod.GET, "/api/products").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
						

						// PROTECTED actions
						.requestMatchers(HttpMethod.POST, "/api/products/**")
						.hasAnyAuthority(Role.ADMIN.name(), Role.SELLER.name())
						.requestMatchers(HttpMethod.PUT, "/api/products/**")
						.hasAnyAuthority(Role.ADMIN.name(), Role.SELLER.name())
						.requestMatchers(HttpMethod.DELETE, "/api/products/**")
						.hasAnyAuthority(Role.ADMIN.name(), Role.SELLER.name())

						// Example for categories
						.requestMatchers("/api/categories")
						.hasAnyAuthority(Role.USER.name(), Role.ADMIN.name(), Role.SELLER.name())
						.requestMatchers("/api/categories/**").hasAuthority(Role.ADMIN.name())

						.requestMatchers("/api/v1/user/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
						.requestMatchers("/api/v1/seller/**").hasAnyAuthority(Role.SELLER.name(), Role.ADMIN.name())

						.anyRequest().authenticated())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
