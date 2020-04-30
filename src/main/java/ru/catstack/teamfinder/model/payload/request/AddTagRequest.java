package ru.catstack.teamfinder.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "Adding tag request")
public class AddTagRequest {
    @ApiModelProperty(value = "Team id")
    private long teamId;

    @NotBlank(message = "New tag cannot be blank")
    @ApiModelProperty(value = "Tag name", allowableValues = "NonEmpty String")
    @Pattern(regexp = "[a-zA-Z0-9]{1,20}", message = "Tag can contain only one word and must be less than 20 characters long")
    private String tag;

    AddTagRequest() {
    }

    AddTagRequest(long teamId, @NotBlank(message = "New tag cannot be blank") String tag) {
        this.teamId = teamId;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public long getTeamId() {
        return teamId;
    }
}
