package ru.catstack.teamfinder.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Image Request", description = "The set image request payload")
public class ImageRequest {
    @NotBlank(message = "Image base64 code cannot be blank")
    @ApiModelProperty(value = "Base64 code", allowableValues = "NonEmpty String")
    private String code;

    ImageRequest() {
    }

    ImageRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
