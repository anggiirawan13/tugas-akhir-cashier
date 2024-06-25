package com.be.handler;

import com.be.app.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;

import java.util.logging.Level;
import java.util.logging.Logger;

public class InternalServerErrorHandler {

    public static BaseResponse InternalServerError(Exception e) {
        Logger.getLogger("InternalServerErrorHandler").log(Level.SEVERE, e.toString());

        return new BaseResponse(false, "INTERNAL_SERVER_ERROR", null);
    }

}
