package com.osbblevymista.api.controllers;

import com.osbblevymista.api.dto.request.AuthRequest;
import com.osbblevymista.api.services.MiyDimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MiyDimService miyDimService;

    @PostMapping
    public ResponseEntity<String> sessionCookieAuth(@RequestBody AuthRequest authRequest) {
        Optional<String> res = miyDimService.auth(authRequest.getLogin(), authRequest.getPass(), authRequest.getChatId());
        return res.map(s -> new ResponseEntity<>(s, HttpStatus.CONFLICT))
                .orElseGet(() -> new ResponseEntity<>("", HttpStatus.OK));
    }

}
