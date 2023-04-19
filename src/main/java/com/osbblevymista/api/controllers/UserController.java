package com.osbblevymista.api.controllers;


import com.osbblevymista.api.dto.response.UserInfoResponse;
import com.osbblevymista.miydim.services.MiyDimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final MiyDimService miyDimService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUsers() {
        List<UserInfoResponse> list = miyDimService.getAllUsers();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping(path = "{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        Boolean res = miyDimService.deleteUser(userId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
