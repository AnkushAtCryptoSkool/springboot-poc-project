package com.ankush.poc.service.interfaces;


import com.ankush.poc.entity.User;
import com.ankush.poc.payload.EmailSchedulerResponse;

public interface UserService {

    User registerUser(User user);

    EmailSchedulerResponse confirmUser(String token);
}
