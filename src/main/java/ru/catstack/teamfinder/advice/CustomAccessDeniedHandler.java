package ru.catstack.teamfinder.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import ru.catstack.teamfinder.model.payload.response.ApiErrorResponse;
import ru.catstack.teamfinder.model.payload.response.ApiResponse;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException {
        var body = new ApiErrorResponse("Access denied", 403, authException.getCause().toString(), req.getServletPath());
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(403);
        res.getWriter().write(mapper.writeValueAsString(new ApiResponse(body)));
    }
}
