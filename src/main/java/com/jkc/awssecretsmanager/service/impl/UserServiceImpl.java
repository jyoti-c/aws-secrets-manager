package com.jkc.awssecretsmanager.service.impl;

import com.jkc.awssecretsmanager.dto.UserDto;
import com.jkc.awssecretsmanager.model.User;
import com.jkc.awssecretsmanager.repository.UserRepository;
import com.jkc.awssecretsmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jyoti.chabria
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Save user
     *
     * @param userDto UserDTO object
     */
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
    }

    /**
     * Find user by username
     *
     * @param username String username
     * @return User object
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
