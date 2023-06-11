package com.jkc.awssecretsmanager.service;

import com.jkc.awssecretsmanager.dto.UserDto;
import com.jkc.awssecretsmanager.model.User;

/**
 * @author jyoti.chabria
 */
public interface UserService {
    void saveUser(UserDto userDto);

    User findByUsername(String username);
}
