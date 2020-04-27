package ru.catstack.auth.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Sending message request")
public class SendMessageRequest {
    @ApiModelProperty(value = "Team id")
    private long teamId;

    @NotBlank(message = "Message cannot be blank")
    @ApiModelProperty(value = "Message text", allowableValues = "NonEmpty String")
    @Length(min = 1, max = 1000, message = "You cannot send empty messages and messages longer than 1000 characters")
    private String message;

    SendMessageRequest(long teamId, @NotBlank(message = "Message cannot be blank") String message) {
        this.teamId = teamId;
        this.message = message;
    }

    SendMessageRequest() {
    }

    public long getTeamId() {
        return teamId;
    }

    public String getMessage() {
        return message;
    }
}
