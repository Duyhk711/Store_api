package com.example.Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CheckConnectionDatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("check-connection")
    public ResponseEntity<Map<String, Object>> checkConnection() {
        Map<String, Object> response = new HashMap<>();
        try{
            jdbcTemplate.execute("select * from users");
            response.put("success", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("success", false);
            response.put("message", "Connection failed");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
