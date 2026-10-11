package com.hubpieces.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "01 — Session")
public class CsrfController {
    @GetMapping("/api/csrf")
    public java.util.Map<String, String> csrf(CsrfToken token) {
        return java.util.Map.of("headerName", token.getHeaderName(), "token", token.getToken());
    }
}
