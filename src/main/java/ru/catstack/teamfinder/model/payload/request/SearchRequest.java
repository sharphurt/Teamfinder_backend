package ru.catstack.teamfinder.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Adding tag request")
public class SearchRequest {
    @NotBlank(message = "New tag cannot be blank")
    @ApiModelProperty(value = "Search string")
    private String searchString;

    SearchRequest(@NotBlank(message = "New tag cannot be blank") String searchString) {
        this.searchString = searchString;
    }

    SearchRequest() {

    }

    public String getSearchString() {
        return searchString;
    }
}
