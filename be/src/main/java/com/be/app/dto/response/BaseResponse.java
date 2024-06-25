package com.be.app.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    public BaseResponse(boolean success, String messages) {
        this.success = success;
        this.messages = messages;
    }

    public BaseResponse(boolean success, String messages, Object data) {
        this.success = success;
        this.messages = messages;
        this.data = data;
    }

    @JsonProperty(namespace = "success")
    private boolean success;

    @JsonProperty(namespace = "messages")
    private String messages;

    @JsonProperty(namespace = "data")
    private Object data;

    @JsonProperty(namespace = "additional_entity")
    private Object additionalEntity;

}
