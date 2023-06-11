package com.jkc.awssecretsmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author jyoti.chabria
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    @NotEmpty(message = "Username should not be empty")
    private String userName;
    @Email
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;
}

