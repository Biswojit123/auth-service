package com.biswojit.autho.autho.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biswojit.autho.autho.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
   Optional<User> findByEmail(String email);
   boolean existsByEmail(String email);
   
}
