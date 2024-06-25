package com.be.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.be.app.dto.response.BaseResponse;
import com.be.helper.NullEmptyChecker;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JWTAuthenticationEntryPointHandler implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        BaseResponse baseResponse;

        String expired = String.valueOf(httpServletRequest.getAttribute("expired"));
        if (NullEmptyChecker.isNotNullOrEmpty(expired)) {
            baseResponse = new BaseResponse(false, "EXPIRED_TOKEN", null);
        } else {
            baseResponse = new BaseResponse(false, "INVALID_TOKEN", null);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(httpServletResponse.getOutputStream(), baseResponse);
    }
}
