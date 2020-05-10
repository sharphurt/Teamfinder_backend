package ru.catstack.teamfinder.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.catstack.teamfinder.exception.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "Team card registration Request", description = "The team card registration request payload")
public class TeamRegistrationRequest {

    @NotBlank(message = "Team name cannot be blank")
    @Pattern(regexp = "[a-zA-Z0-9а-яА-Я\\s]{2,30}", message = "The team name can only include uppercase and lowercase letters of the English and Russian alphabet and numbers and length must be between 2 and 30 character")
    @ApiModelProperty(value = "A valid team name", required = true, allowableValues = "NonEmpty String")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @ApiModelProperty(value = "Team description", required = true, allowableValues = "NonEmpty String")
    @Length(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    @ApiModelProperty(value = "Picture base64 code")
    private String picCode;

    @ApiModelProperty(value = "Team tags")
    private List<String> tags = new ArrayList<>();

    public TeamRegistrationRequest(String name, String description, String picCode, List<String> tags) {
        this.name = name;
        this.description = description;
        this.picCode = picCode;
        this.tags = tags;
    }

    public TeamRegistrationRequest() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPicCode() {
        return picCode;
    }

    public List<String> getTags() {
        return tags;
    }
}
