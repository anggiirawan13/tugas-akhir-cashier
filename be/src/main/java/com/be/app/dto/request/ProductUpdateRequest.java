package com.be.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

@Data
@NonNull
public class ProductUpdateRequest {

    @JsonProperty(value = "product_code")
    private String productCode;

    @JsonProperty(value = "product_name")
    private String productName;

    @JsonProperty(value = "price")
    private double price;

    @JsonProperty(value = "stock")
    private int stock;

    @JsonProperty(value = "category_id")
    private int categoryID;

    @JsonProperty(value = "status")
    private String status;

}
