package com.ankush.poc.controller;

import com.ankush.poc.entity.User;
import com.ankush.poc.payload.EmailSchedulerResponse;
import com.ankush.poc.response.HttpResponse;
import com.ankush.poc.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user){
        User registeredUser = null;
        try {
            registeredUser = userService.registerUser(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(HttpResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .timeStamp(String.valueOf(LocalDateTime.now()))
                            .message("Something Went Wrong")
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build());
        }
        return ResponseEntity.created(URI.create(""))
                .body(HttpResponse.builder()
                        .status(HttpStatus.CREATED)
                        .timeStamp(String.valueOf(LocalDateTime.now()))
                        .message("User Registered SuccessFully")
                        .statusCode(HttpStatus.CREATED.value())
                        .data((Map<?, ?>) new HashMap<>().put("user",registeredUser))
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> confirmUser(@RequestParam("token") String token){
        EmailSchedulerResponse response = userService.confirmUser(token);
        return ResponseEntity.ok()
                .body(HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .timeStamp(String.valueOf(LocalDateTime.now()))
                        .message("User Confimed")
                        .statusCode(HttpStatus.OK.value())
                        .data((Map<?, ?>) new HashMap<>().put("Job Data",response))
                        .build());
    }



}
