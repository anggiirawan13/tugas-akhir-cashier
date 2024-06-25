package com.be.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
@NonNull
public class CategoryUpdateRequest implements Serializable {

    @JsonProperty(value = "category_code")
    private String categoryCode;

    @JsonProperty(value = "category_name")
    private String categoryName;

    @JsonProperty(value = "status")
    private String status;

}
