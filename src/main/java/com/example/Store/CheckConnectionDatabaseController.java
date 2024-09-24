package com.example.Store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckConnectionDatabaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("check-connection")
    public ResponseEntity<String> checkConnection() {
        try{
            jdbcTemplate.execute("select 1");
            return new ResponseEntity<String>("success", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<String>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
