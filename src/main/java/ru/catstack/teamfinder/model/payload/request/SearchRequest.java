package ru.catstack.teamfinder.model.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Adding tag request")
public class SearchRequest {
    @NotBlank(message = "New tag cannot be blank")
    @ApiModelProperty(value = "Search string")
    private String searchString;

    private int from;

    private int count;

    SearchRequest(@NotBlank(message = "New tag cannot be blank") String searchString, int from, int count) {
        this.searchString = searchString;
        this.from = from;
        this.count = count;
    }

    SearchRequest() {

    }

    public String getSearchString() {
        return searchString;
    }

    public int getFrom() {
        return from;
    }

    public int getCount() {
        return count;
    }
}
