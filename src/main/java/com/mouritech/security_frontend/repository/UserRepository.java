package com.mouritech.security_frontend.repository;

import java.util.List;
import java.util.Optional;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.mouritech.security_frontend.entity.Role;
import com.mouritech.security_frontend.entity.User;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, UUID> {

 Optional<User> findByEmail(String email);
 //Optional<User> findByRole(Role role);
 List<User> findByRole(Role role);
 void deleteByEmail(String email);
 boolean existsByEmail(String email);

 
 
}