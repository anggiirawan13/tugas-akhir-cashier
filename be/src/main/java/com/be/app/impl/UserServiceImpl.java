package com.be.app.impl;

import com.be.app.dto.request.UserLoginRequest;
import com.be.app.dto.request.UserRegisterRequest;
import com.be.app.dto.request.UserUpdateRequest;
import com.be.app.dto.response.BaseResponse;
import com.be.app.entity.UserEntity;
import com.be.app.repository.UserRepository;
import com.be.app.service.UserService;
import com.be.constanta.ResponseMessagesConst;
import com.be.constanta.RoleConst;
import com.be.constanta.StatusConst;
import com.be.handler.InternalServerErrorHandler;
import com.be.helper.DateHelper;
import com.be.helper.NullEmptyChecker;
import com.be.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public BaseResponse userRegister(UserRegisterRequest request) {
        try {
            UserEntity newUser = userRepository.findByUsername(request.getUsername());
            if (NullEmptyChecker.isNotNullOrEmpty(newUser)) {
                return new BaseResponse(false, ResponseMessagesConst.INSERT_FAILED.toString());
            } else {
                newUser.setUsername(request.getUsername());
                newUser.setFullname(request.getFullname());
                newUser.setEmail(request.getEmail());

                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                String passEncode = bCryptPasswordEncoder.encode(request.getPassword());

                newUser.setPassword(passEncode);
                newUser.setUuid(UUID.randomUUID().toString());

                if (request.getRole() != null && request.getRole().equalsIgnoreCase(RoleConst.ADMIN.toString())) {
                    newUser.setRole(RoleConst.ADMIN.toString());
                } else {
                    newUser.setRole(RoleConst.CASHIER.toString());
                }

                if (request.getStatus() != null && request.getStatus().equalsIgnoreCase(StatusConst.ACTIVE.toString())) {
                    newUser.setStatus(StatusConst.ACTIVE.toString());
                } else {
                    newUser.setStatus(StatusConst.INACTIVE.toString());
                }

                Timestamp dateNow = DateHelper.getTimestampNow();

                newUser.setCreatedAt(dateNow);
                newUser.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                newUser.setModifiedAt(dateNow);
                newUser.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

                UserEntity user = userRepository.save(newUser);

                if (NullEmptyChecker.isNotNullOrEmpty(user)) {
                    return new BaseResponse(true, ResponseMessagesConst.INSERT_SUCCESS.toString(), user);
                }

                return new BaseResponse(false, ResponseMessagesConst.INSERT_FAILED.toString());
            }
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse userLogin(UserLoginRequest request) {
        try {
            UserEntity user = userRepository.findByUsername(request.getUsername());
            HashMap<String, Object> dataLogin = new HashMap<>();
            dataLogin.put("access_token", jwtUtil.generateToken(request.getUsername()));
            dataLogin.put("refresh_token", "");
            dataLogin.put("fullname", user.getFullname());

            if (NullEmptyChecker.isNotNullOrEmpty(dataLogin)) {
                return new BaseResponse(true, "LOGIN_SUCCESS", dataLogin);
            }

            return new BaseResponse(false, "LOGIN_FAILED");
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse getUser(int page, int limit, String search) {
        try {
            List<UserEntity> listUser;
            HashMap<String, Object> addEntity = new HashMap<>();
            if (page < 0 || NullEmptyChecker.isNullOrEmpty(limit)) {
                listUser = userRepository.findAll();
            } else {
                Pageable pageable = PageRequest.of(page, limit);
                Page<UserEntity> pageUser = userRepository.findAll(pageable);
                listUser = pageUser.toList();

                addEntity.put("totalPage", pageUser.getTotalPages());
                addEntity.put("totalData", pageUser.getTotalElements());
                addEntity.put("numberOfData", pageUser.getNumberOfElements());
                addEntity.put("number", pageUser.getNumber());
            }

            if (NullEmptyChecker.isNotNullOrEmpty(listUser)) {
                return new BaseResponse(true, ResponseMessagesConst.DATA_FOUND.toString(), listUser, addEntity);
            }

            return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse getUserByUUID(String uuid) {
        try {
            UserEntity listUser = userRepository.findByUuid(uuid);

            if (NullEmptyChecker.isNotNullOrEmpty(listUser)) {
                return new BaseResponse(true, ResponseMessagesConst.DATA_FOUND.toString(), listUser);
            }

            return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse updateUser(String uuid, UserUpdateRequest request) {
        try {
            UserEntity newUser = userRepository.findByUuid(uuid);
            if (NullEmptyChecker.isNullOrEmpty(newUser)) {
                return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
            } else {
                newUser = userRepository.findByUsername(request.getUsername());
                if (NullEmptyChecker.isNotNullOrEmpty(newUser)) {
                    return new BaseResponse(false, ResponseMessagesConst.UPDATE_FAILED.toString());
                } else {
                    newUser.setUsername(request.getUsername());
                    newUser.setFullname(request.getFullname());
                    newUser.setEmail(request.getEmail());

                    if (!request.getPassword().equalsIgnoreCase(newUser.getPassword())) {
                        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                        String passEncode = bCryptPasswordEncoder.encode(request.getPassword());

                        newUser.setPassword(passEncode);
                    }

                    if (request.getRole() != null && request.getRole().equalsIgnoreCase(RoleConst.ADMIN.toString())) {
                        newUser.setRole(RoleConst.ADMIN.toString());
                    } else {
                        newUser.setRole(RoleConst.CASHIER.toString());
                    }

                    if (request.getStatus() != null && request.getStatus().equalsIgnoreCase(StatusConst.ACTIVE.toString())) {
                        newUser.setStatus(StatusConst.ACTIVE.toString());
                    } else {
                        newUser.setStatus(StatusConst.INACTIVE.toString());
                    }

                    Timestamp dateNow = DateHelper.getTimestampNow();

                    newUser.setCreatedAt(dateNow);
                    newUser.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
                    newUser.setModifiedAt(dateNow);
                    newUser.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

                    UserEntity user = userRepository.save(newUser);

                    if (NullEmptyChecker.isNotNullOrEmpty(user)) {
                        return new BaseResponse(true, ResponseMessagesConst.INSERT_SUCCESS.toString(), user);
                    }
                }
            }

            return new BaseResponse(false, ResponseMessagesConst.INSERT_FAILED.toString());
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }

    @Override
    public BaseResponse deleteUser(String uuid) {
        try {
            UserEntity user = userRepository.findByUuid(uuid);
            if (NullEmptyChecker.isNullOrEmpty(user)) {
                return new BaseResponse(false, ResponseMessagesConst.DATA_NOT_FOUND.toString());
            } else {
                userRepository.delete(user);
                return new BaseResponse(true, ResponseMessagesConst.UPDATE_SUCCESS.toString());
            }
        } catch (Exception e) {
            return InternalServerErrorHandler.InternalServerError(e);
        }
    }
}
