package com.example.userservice.controller;

import com.example.userservice.feignClientModel.UserShortInfoDto;
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

    @GetMapping("/getAllUsers")
    public List<ViewUserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<Void> setEnabledUser(@PathVariable Long id) {
        userService.setUserEnabledState(id, true);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.setUserEnabledState(id, false);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/createUser")
    public ResponseEntity<Long> createUser(@RequestBody CreateUserDto userDto) {
        var userId = userService.createUser(userDto);
        return ResponseEntity.ok(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto userDto) {
        userService.updateUserInfo(userDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> userExists(@PathVariable Long id) {
        return ResponseEntity.ok(userService.checkUserExisting(id));
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getUserName(@PathVariable Long id) {
        var name = userService.getUserName(id);
        return ResponseEntity.ok(name);
    }

    @GetMapping("/getAllUsersShortInfo")
    public List<UserShortInfoDto> getAllUsersShortInfo() {
        return userService.getAllUsersShortInfo();
    }
}
