package com.example.Store.model.mapper;

import com.example.Store.entity.User;
import com.example.Store.model.dto.UserDTO.UserDTOResponse;

public class UserMapper {
    public static UserDTOResponse toUserDTORespone(User user) {
        return UserDTOResponse.builder().email(user.getEmail())
                .build();
    }
}
