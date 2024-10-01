package com.example.Store.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Store.entity.User;
import com.example.Store.model.dto.UserDTO.UserDTOLogin;
import com.example.Store.model.dto.UserDTO.UserDTOResponse;
import com.example.Store.model.mapper.UserMapper;
import com.example.Store.repository.UserRepository;
import com.example.Store.service.UserService;
import com.example.Store.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtUtil;

    @Override
    public Map<String, UserDTOResponse> login(UserDTOLogin userDTOLogin) throws Exception{
        User userOptional = userRepository.findByEmail(userDTOLogin.getEmail());
        boolean isAuthen = false;
        if (userOptional == null) {
            throw new RuntimeException("Email not found");
        }else {
            User user = userOptional;
            if (passwordEncoder.matches(userDTOLogin.getPassword(), user.getPassword())) {
                isAuthen = true;
            }
        }
        
        if (!isAuthen) {
            throw new RuntimeException( "message: " + "Invalid email or password");
        }

        return buildDTOResponse(userOptional);
    }



    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private Map<String, UserDTOResponse> buildDTOResponse(User user) {
        Map<String, UserDTOResponse> wrapper = new HashMap<>();
        UserDTOResponse userDTOResponse = UserMapper.toUserDTORespone(user);
        Long expriedTime = 24*60*60L;
        String token = jwtUtil.generateToken(user, expriedTime);
        System.out.println(token);
        userDTOResponse.setToken(token);
        // userDTOResponse.setPassword(passwordEncoder.encode(user.getPassword()));
        wrapper.put("user", userDTOResponse);
        return wrapper;
    }

}
