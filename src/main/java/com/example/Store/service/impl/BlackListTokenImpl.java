package com.example.Store.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.Store.entity.BlackLitsToken;
import com.example.Store.repository.BlackListRepository;
import com.example.Store.service.BlackListSerVice;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlackListTokenImpl implements BlackListSerVice {


    private final BlackListRepository blackListRepository;


    @Override
    public void blacklistToken(String token, Date expiryDate) {
        String tokenWithoutBearer = token.startsWith("Bearer ") ? token.substring(7).trim() : token;

        BlackLitsToken blackLitsToken = new BlackLitsToken();
        blackLitsToken.setToken(tokenWithoutBearer);
        blackLitsToken.setExpiryDate(expiryDate);
        blackListRepository.save(blackLitsToken);
    }

    
    public boolean isTokenBlacklisted(String token) {
        String tokenWithoutBearer = token.startsWith("Bearer ") ? token.substring(7).trim() : token;

        Optional<BlackLitsToken> blacklistedToken = blackListRepository.findByToken(tokenWithoutBearer);
    
        System.out.println("Token checked: " + tokenWithoutBearer);
        if (blacklistedToken.isPresent()) {
            System.out.println("Token is found in blacklist: " + blacklistedToken.get().getToken());
        } else {
            System.out.println("Token not found in blacklist");
        }
    
        return blacklistedToken.isPresent();
    }
}
