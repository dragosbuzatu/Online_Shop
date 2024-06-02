package com.example.Shop.repository;

import com.example.Shop.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findUserByUsername(String username);
}

