package com.osbblevymista.api.controllers;

import com.osbblevymista.api.dto.request.AuthRequest;
import com.osbblevymista.api.services.MiyDimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final MiyDimService miyDimService;

    @PostMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sessionCookieAuth(@RequestBody AuthRequest authRequest) {
        Optional<String> res = miyDimService.auth(authRequest);
        return res.map(s -> new ResponseEntity<>(s, HttpStatus.CONFLICT))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
    }

}
