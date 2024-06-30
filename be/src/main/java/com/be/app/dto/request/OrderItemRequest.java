package com.be.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

@Data
@NonNull
public class OrderItemRequest {

    @JsonProperty("product_id")
    private Long productID;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("total_price")
    private double totalPrice;

    @JsonProperty("price")
    private double price;

}
