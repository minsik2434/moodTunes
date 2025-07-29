package com.moodtunes.apiserver.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodtunes.apiserver.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class HttpResponseUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    private HttpResponseUtil(){

    }

    public static void sendErrorResponse(HttpServletResponse response, HttpStatus httpStatus, String errorCode, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), errorCode, message);
        String responseString = mapper.writeValueAsString(errorResponse);
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(responseString);
        response.getWriter().flush();
    }
}
