package com.example.Store.controller;

import com.example.Store.model.dto.UserDTO.UserDTOLogin;
import com.example.Store.model.dto.UserDTO.UserDTOResponse;
import com.example.Store.service.BlackListSerVice;
import com.example.Store.service.UserService;
import com.example.Store.util.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BlackListSerVice blackListSerVice;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("auth/login")
    public Map<String, UserDTOResponse> login(@RequestBody  UserDTOLogin userDTOLogin) throws Exception {
        return userService.login(userDTOLogin);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(HttpServletRequest request) throws Exception {
        final String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7).trim(); 
        } else {
            System.out.println("Authorization header not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header not found");
        }

        if (!jwtTokenUtil.validateToken(token)) {
            System.out.println("Token is invalid or expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
        }

        if (blackListSerVice.isTokenBlacklisted(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token is already logged out");
        }

        blackListSerVice.blacklistToken(token, new Date(System.currentTimeMillis() + 1000 * 60 *60 * 5));

        return ResponseEntity.ok("Logged out successfully");
    }

}
