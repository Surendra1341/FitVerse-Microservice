package com.ssc.userservice.repo;

import com.ssc.userservice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);

    Boolean existsByKeycloakId(String keycloakId);

    User findByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email);
}
