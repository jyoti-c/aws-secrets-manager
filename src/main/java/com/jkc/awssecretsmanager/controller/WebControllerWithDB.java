package com.jkc.awssecretsmanager.controller;

import com.jkc.awssecretsmanager.config.AwsConfig;
import com.jkc.awssecretsmanager.dto.UserDto;
import com.jkc.awssecretsmanager.model.User;
import com.jkc.awssecretsmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author jyoti.chabria
 */
@Controller
public class WebControllerWithDB {

    /**
     * @author jyoti.chabria
     */
    private UserService userService;


    public WebControllerWithDB(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        AwsConfig awsConfig = new AwsConfig();
        return "index";

    }

    // handler method to handle user registration form request
    @GetMapping("/register2")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register2";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register2/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
        BindingResult result,
        Model model) {
        User existingUser = userService.findByUsername(userDto.getUserName());

        if (existingUser != null && existingUser.getUsername() != null
            && !existingUser.getUsername().isEmpty()) {
            result.rejectValue("userName", null,
                "There is already an account registered with the same username");
        }

        if (result.hasErrors()) {
            model.addAttribute("userName", userDto);
            return "redirect:/register2?failure";
        }

        userService.saveUser(userDto);
        return "redirect:/register2?success";
    }

    // handler method to handle login request
    @GetMapping("/login2")
    public String login() {
        return "login2";
    }

    @PostMapping("/login2")
    public String validateLogin(@Valid @ModelAttribute("user") UserDto userDto) {
        User existingUser = userService.findByUsername(userDto.getUserName());
        if (existingUser != null && existingUser.getUsername() != null
            && !existingUser.getUsername().isEmpty()) {
            if (existingUser.getPassword().equals(userDto.getPassword())) {
                return "redirect:/login?success";
            }
        }
        return "redirect:/login2?error";
    }


}

