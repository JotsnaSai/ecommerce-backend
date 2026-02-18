package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.RegisterRequest;
import com.ecommerce.backend.dto.UserResponse;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setRole("USER");

        // Save to database
        User savedUser = userRepository.save(user);

        // Convert to response DTO
        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getPhone(),
                savedUser.getRole()
        );
    }
}