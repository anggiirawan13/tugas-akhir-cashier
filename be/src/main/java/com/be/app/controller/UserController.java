package com.be.app.controller;

import com.be.app.dto.request.UserLoginRequest;
import com.be.app.dto.request.UserRegisterRequest;
import com.be.app.dto.request.UserUpdateRequest;
import com.be.app.dto.response.BaseResponse;
import com.be.app.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/register")
    private BaseResponse userRegister(@RequestBody UserRegisterRequest request) {
        return userServiceImpl.userRegister(request);
    }

    @PostMapping(value = "/login")
    private BaseResponse userLogin(@RequestBody UserLoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            return new BaseResponse(false, "LOGIN_FAILED", null);
        }

        return userServiceImpl.userLogin(request);
    }

    @GetMapping
    private BaseResponse getUser(@RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "limit", required = false, defaultValue = "0") int limit, @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return userServiceImpl.getUser(page, limit, search);
    }

    @GetMapping("/{uuid}")
    private BaseResponse getUserByUUID(@PathVariable(value = "uuid") String uuid) {
        return userServiceImpl.getUserByUUID(uuid);
    }

    @PutMapping("/{uuid}")
    private BaseResponse updateUser(@PathVariable(value = "uuid") String uuid, @RequestBody UserUpdateRequest request) {
        return userServiceImpl.updateUser(uuid, request);
    }

    @DeleteMapping("/{uuid}")
    private BaseResponse deleteUser(@PathVariable(value = "uuid") String uuid) {
        return userServiceImpl.deleteUser(uuid);
    }
}
