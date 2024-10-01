package com.example.Store.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Entity
@Data
@Table(name = "blacklisted_tokens")
public class BlackLitsToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Date expiryDate;
}
