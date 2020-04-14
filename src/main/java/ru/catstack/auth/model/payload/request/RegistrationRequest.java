package ru.catstack.auth.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "Registration Request", description = "The registration request payload")
public class RegistrationRequest {

    @NotBlank(message = "Username cannot be blank")
    @NotNull(message = "Username cannot be null")
    @ApiModelProperty(value = "A valid username", required = true, allowableValues = "NonEmpty String")
    private String username;

    @NotBlank(message = "First name cannot be blank")
    @NotNull(message = "First name cannot be null")
    @ApiModelProperty(value = "A valid first name", required = true, allowableValues = "NonEmpty String")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @NotNull(message = "Last name  cannot be null")
    @ApiModelProperty(value = "A valid last name", required = true, allowableValues = "NonEmpty String")
    private String lastName;

    @ApiModelProperty(value = "A valid age", required = true)
    @Min(value = 12, message = "Only users over 12 years old are allowed")
    @Max(value = 127, message = "Age cannot be higher than 127 years")
    private Integer age;

    @NotBlank(message = "Email cannot be blank")
    @NotNull(message = "Email cannot be null")
    @ApiModelProperty(value = "A valid email", required = true, allowableValues = "NonEmpty String")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @NotNull(message = "Password cannot be null")
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
