package com.foodei.project.service;

import com.foodei.project.entity.Token;
import com.foodei.project.entity.User;
import com.foodei.project.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    // Lưu token
    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    // Lấy thông tin của token
    public Optional<Token> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    // Confirm time token
    public void setConfirmedAt(String token) {
        tokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }

    // Lấy user theo token
    public User getUserByToken(String token){
        Optional<Token> tokenOptional = getToken(token);
        return tokenOptional.get().getUser();
    }
}
