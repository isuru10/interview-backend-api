package com.example.hirdaramani.security;

import com.example.hirdaramani.service.EmployeeService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    private final SecretKey secretKey;
    private final EmployeeService employeeService;

    public SecurityInterceptor(SecretKey secretKey, EmployeeService employeeService) {
        this.secretKey = secretKey;
        this.employeeService = employeeService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String endPoint = request.getServletPath();

        if(endPoint.equalsIgnoreCase("/api/v1/employees/login")) return true;
        if(endPoint.equalsIgnoreCase("/error")) return true;

        String authorization = request.getHeader("Authorization");
        if(authorization != null){
            if(authorization.toUpperCase().startsWith("BEARER")) {
                String token = authorization.substring(7);
                JwtParserBuilder jwtParserBuilder = Jwts.parser().requireIssuer("hirdaramani");
                try {
                    Jws<Claims> jws = jwtParserBuilder.verifyWith(secretKey).build().parseSignedClaims(token);
                    String username = jws.getPayload().getSubject();
                    String password = "Hirdaramani123";
//                    if (employeeService.findEmployeeByName(username, password)) return true;
                }catch (JwtException exp){
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
}
