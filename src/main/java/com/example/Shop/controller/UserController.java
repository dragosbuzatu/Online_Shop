package com.example.Shop.controller;


import com.example.Shop.dto.UserAuthenticateRequestDTO;
import com.example.Shop.dto.UserRequestDTO;
import com.example.Shop.dto.UserResponseDTO;
import com.example.Shop.service.impl.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            return ResponseEntity.created(new URI("")).body(userService.saveUser(userRequestDTO));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody UserAuthenticateRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.authenticateUser(userRequestDTO));
    }

    @PatchMapping("/{username}")
    public void updateUser() {

    }

    //    @ApiResponses({
//            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = Employee.class)) }),
//            @ApiResponse(responseCode = "404", description = "Employee not found",
//                    content = @Content) })
    @DeleteMapping("/{username}")
    public void deleteUser() {

    }
}
