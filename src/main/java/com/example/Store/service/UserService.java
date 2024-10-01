package com.example.Store.service;

import com.example.Store.entity.User;
import com.example.Store.model.dto.UserDTO.UserDTOLogin;
import com.example.Store.model.dto.UserDTO.UserDTOResponse;

import java.util.Map;

public interface UserService {
    public Map<String, UserDTOResponse> login(UserDTOLogin userDTOLogin) throws Exception;
    User findByEmail(String email);

}
