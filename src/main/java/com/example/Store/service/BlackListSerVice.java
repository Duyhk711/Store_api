package com.example.Store.service;

import java.util.Date;

public interface BlackListSerVice {
    public void blacklistToken(String token, Date expiryDate);

    public boolean isTokenBlacklisted(String token);
}
