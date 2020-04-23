package ru.catstack.auth.model.payload.request.setRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ApiModel(value = "Update age Request", description = "The update age request payload")
public class SetAge {
    @ApiModelProperty(value = "A valid age", required = true)
    @Min(value = 12, message = "Only users over 12 years old are allowed")
    @Max(value = 127, message = "Age cannot be higher than 127 years")
    private Integer age;

    public SetAge(int firstName) {
        this.age = firstName;
    }

    public SetAge() {

    }

    public byte getAge() {
        return age.byteValue();
    }
}

