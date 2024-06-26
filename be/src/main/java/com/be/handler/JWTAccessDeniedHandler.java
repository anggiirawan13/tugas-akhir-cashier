package com.be.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.be.app.dto.response.BaseResponse;
import com.be.helper.NullEmptyChecker;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        BaseResponse baseResponse;

        String expired = String.valueOf(request.getAttribute("expired"));
        if (NullEmptyChecker.isNotNullOrEmpty(expired)) {
            baseResponse = new BaseResponse(false, "EXPIRED_TOKEN", null);
        } else {
            baseResponse = new BaseResponse(false, "INVALID_TOKEN", null);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), baseResponse);
    }
}
