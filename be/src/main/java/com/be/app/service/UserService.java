package com.be.app.service;

import com.be.app.dto.request.UserLoginRequest;
import com.be.app.dto.request.UserRegisterRequest;
import com.be.app.dto.request.UserUpdateRequest;
import com.be.app.dto.response.BaseResponse;

public interface UserService {
    BaseResponse userRegister(UserRegisterRequest request);

    BaseResponse userLogin(UserLoginRequest request);

    BaseResponse getUser(int page, int limit, String search);

    BaseResponse getUserByUUID(String uuid);

    BaseResponse updateUser(String uuid, UserUpdateRequest request);

    BaseResponse deleteUser(String uuid);

}
