package com.example.userservice.controller;

import com.example.userservice.model.CreateUserDto;
import com.example.userservice.model.UpdateUserDto;
import com.example.userservice.model.ViewUserDto;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @GetMapping
    public List<ViewUserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> setEnabledUser(@PathVariable Long id) {
        userService.setUserEnabledState(id, true);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.setUserEnabledState(id, false);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody CreateUserDto userDto) {
        var userId = userService.createUser(userDto);
        return ResponseEntity.ok(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto userDto) {
        userService.updateUserInfo(userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> userExists(@PathVariable Long id) {
        if (!userService.checkUserExisting(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or inactive");
        }
        return ResponseEntity.ok().build();
    }
}
