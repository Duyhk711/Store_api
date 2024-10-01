package com.example.Store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Store.entity.BlackLitsToken;

@Repository
public interface BlackListRepository extends JpaRepository<BlackLitsToken, Integer> {
    Optional<BlackLitsToken> findByToken(String token);
}
