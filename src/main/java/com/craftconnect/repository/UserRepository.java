package com.craftconnect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craftconnect.entity.Role;
import com.craftconnect.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
    long countByRole(Role role);
    
    List<User> findTop5ByRoleOrderByIdDesc(Role role);
}