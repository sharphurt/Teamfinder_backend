package ru.catstack.auth.model.payload;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

public class SetUserInfoRequest {
    @ApiModelProperty(value = "Information about user", allowableValues = "NonEmpty String")
    @Length(max = 500)
    private String aboutMe;

    public String getAboutMe() {
        return aboutMe;
    }
}
