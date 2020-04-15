package ru.catstack.auth.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@ApiModel(value = "Registration Request", description = "The registration request payload")
public class RegistrationRequest {

    @NotBlank(message = "Username cannot be blank")
    @Length(min = 2, max = 30, message = "Username length must be between 2 and 30 characters")
    @Pattern(regexp = "[a-zA-Z0-9]", message = "The username can only include uppercase and lowercase letters of the English alphabet and numbers")
    @ApiModelProperty(value = "A valid username", required = true, allowableValues = "NonEmpty String")
    private String username;

    @NotBlank(message = "First name cannot be blank")
    @ApiModelProperty(value = "A valid first name", required = true, allowableValues = "NonEmpty String")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @ApiModelProperty(value = "A valid last name", required = true, allowableValues = "NonEmpty String")
    private String lastName;

    @ApiModelProperty(value = "A valid age", required = true)
    @Min(value = 12, message = "Only users over 12 years old are allowed")
    @Max(value = 127, message = "Age cannot be higher than 127 years")
    private Integer age;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Length(min = 8, max = 50, message = "Password length must be between 8 and 50 characters")
    @ApiModelProperty(value = "A valid password string", required = true, allowableValues = "NonEmpty String")
    private String password;

    public RegistrationRequest(String username, String email, String firstName, String lastName, Integer age, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public RegistrationRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Byte getAge() {
        return age.byteValue();
    }
}
