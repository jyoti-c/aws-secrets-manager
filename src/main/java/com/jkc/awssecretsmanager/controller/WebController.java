package com.jkc.awssecretsmanager.controller;


import com.jkc.awssecretsmanager.config.AwsConfig;
import com.jkc.awssecretsmanager.dto.UserDto;
import com.jkc.awssecretsmanager.model.User;
import com.jkc.awssecretsmanager.repository.UserRepository;
import com.jkc.awssecretsmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WebController {

    AwsConfig awsConfig;
    //    @Value("${testKey}")
//    private String name;
    private UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        awsConfig = new AwsConfig();
        return "index";

    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto) {
        String secretArn = awsConfig.createSecret(userDto.getUserName(), userDto.getPassword());
        if (secretArn.isEmpty()) {
            return "redirect:/register?failure";
        } else {
            return "redirect:/register?success";
        }
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String validateLogin(@Valid @ModelAttribute("user") UserDto userDto,
        BindingResult result,
        Model model) {
        System.out.println("Checking for secret name " + userDto.getUserName());
        if (awsConfig.getSecret(userDto.getUserName(), userDto.getPassword())) {
            return "redirect:/login?success";
        } else {
            return "redirect:/login?error";
        }
    }
}
