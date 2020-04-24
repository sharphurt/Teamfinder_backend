package ru.catstack.auth.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Adding role request")
public class AddRoleRequest {
    @ApiModelProperty(value = "Member id")
    private long memberId;

    @NotBlank(message = "New role cannot be blank")
    @ApiModelProperty(value = "Role name", allowableValues = "NonEmpty String")
    private String role;

    AddRoleRequest() {
    }

    AddRoleRequest(long memberId, @NotBlank(message = "New role cannot be blank") String role) {
        this.memberId = memberId;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public long getMemberId() {
        return memberId;
    }
}
