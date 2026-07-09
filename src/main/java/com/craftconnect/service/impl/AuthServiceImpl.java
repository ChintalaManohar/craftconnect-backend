package com.craftconnect.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.craftconnect.dto.LoginRequest;
import com.craftconnect.dto.LoginResponse;
import com.craftconnect.dto.RegisterRequest;
import com.craftconnect.dto.RegisterResponse;
import com.craftconnect.entity.User;
import com.craftconnect.exception.EmailAlreadyExistsException;
import com.craftconnect.exception.UserNotFoundException;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.security.JwtService;
import com.craftconnect.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthServiceImpl(UserRepository userRepository,
	                       PasswordEncoder passwordEncoder,JwtService jwtService) {
	    this.userRepository = userRepository;
	    this.passwordEncoder = passwordEncoder;
	    this.jwtService= jwtService;
	}

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
        	throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
        
        

        return new RegisterResponse("User Registered Successfully");
    }
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                    new UserNotFoundException("User not found"));
        
        if (!user.isActive()) {
            throw new RuntimeException(
                    "Your account has been deactivated. Please contact the administrator.");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid Password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token,
                user.getFullName(),
                user.getEmail(),
                user.getRole());
    }
}