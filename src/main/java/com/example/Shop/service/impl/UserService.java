package com.example.Shop.service.impl;

import com.example.Shop.Model.User;
import com.example.Shop.dto.UserAuthenticateRequestDTO;
import com.example.Shop.dto.UserRequestDTO;
import com.example.Shop.dto.UserResponseDTO;
import com.example.Shop.exceptions.UserException;
import com.example.Shop.repository.UserRepository;
import com.example.Shop.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String authenticateUser(UserAuthenticateRequestDTO userRequestDTO)
            throws UserException.UsernameNotFound, UserException.PasswordIncorrect {
        Optional<User> userOptional = userRepository.findUserByUsername(userRequestDTO.getUsername());
        if (userOptional.isEmpty()) {
            throw new UserException.UsernameNotFound("username " + userRequestDTO.getUsername() + " not found");
        }
        User user = userOptional.get();
        if (!passwordEncoder.matches(userRequestDTO.getPassword(), user.getPassword())) {
            throw new UserException.PasswordIncorrect("given password is incorrect");
        }
        Set<String> authorities = new HashSet<>();
        authorities.add(user.getRoles());
        return jwtProvider.generateToken(user.getUsername(), (long) 500000, authorities);
    }

    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) throws UserException.UsernameAlreadyExists {
        if (userRepository.existsById(userRequestDTO.getUsername())) {
            throw new UserException.UsernameAlreadyExists("username " +
                    userRequestDTO.getUsername() +" already exists");
        }
        return convertToUserDTO(userRepository.save(convertToUserModel(userRequestDTO)));
    }

    public UserResponseDTO getUser(String username) throws UserException.UsernameNotFound {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UserException.UsernameNotFound("username " + username + " not found");
        }
        String usernameAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!usernameAuth.equals(username)) {
            throw new UserException.UserUnauthorized("you can not access user: " + username);
        }
        return convertToUserDTO(userOptional.get());
    }

    private UserResponseDTO convertToUserDTO(User user) {
        return UserResponseDTO.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public void updateUser(UserRequestDTO userRequestDTO) {

    }


    private User convertToUserModel(UserRequestDTO userRequestDTO) {
        return User.builder()
                .username(userRequestDTO.getUsername())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .roles("ROLE_USER")
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .dateOfBirth(userRequestDTO.getDateOfBirth())
                .phoneNumber(userRequestDTO.getPhoneNumber())
                .build();
    }
}

