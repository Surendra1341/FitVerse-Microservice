package com.ssc.userservice.service;


import com.ssc.userservice.dto.RegisterRequest;
import com.ssc.userservice.dto.UserResponse;
import com.ssc.userservice.model.User;
import com.ssc.userservice.repo.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {


    private UserRepository userRepository;


    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()-> new RuntimeException("User not found!")
        );
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }

    public UserResponse register( RegisterRequest registerRequest) {

        if(userRepository.existsByEmail(registerRequest.getEmail())){
            User existingUser  = userRepository.findByEmail(registerRequest.getEmail());
            UserResponse userResponse = new UserResponse();

            userResponse.setId(existingUser.getId());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setPassword(existingUser.getPassword());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            userResponse.setUpdatedAt(existingUser.getUpdatedAt());
            userResponse.setKeycloakId(existingUser.getKeycloakId());
            return userResponse;
        }

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setKeycloakId(registerRequest.getKeycloakId());

        User savedUser =userRepository.save(user);

        UserResponse userResponse = new UserResponse();

        userResponse.setId(savedUser.getKeycloakId());
        userResponse.setId(savedUser.getId());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());
        return userResponse;

    }

    public Boolean existByUserId(String userId) {

        return userRepository.existsByKeycloakId(userId);
    }
}
