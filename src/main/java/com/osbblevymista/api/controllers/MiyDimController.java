package com.osbblevymista.api.controllers;

import com.osbblevymista.api.dto.request.AdminInfoRequest;
import com.osbblevymista.api.dto.response.AdminInfoResponse;
import com.osbblevymista.api.dto.response.UserInfoResponse;
import com.osbblevymista.api.services.MiyDimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/miydim")
@RequiredArgsConstructor
@RestController
public class MiyDimController {

//    private final MiyDimService miyDimService;
//
//    @PostMapping(path = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> addAdmin(@RequestBody AdminInfoRequest adminInfoRequest) {
//        Boolean res = miyDimService.addAdmin(adminInfoRequest);
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }
//
//    @DeleteMapping(path = "/admin/{adminId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> deleteAdmin(@PathVariable("adminId") Long adminAd) {
//        Boolean res = miyDimService.delete(adminAd);
//        return new ResponseEntity<>(res, HttpStatus.OK);
//    }
//
//    @GetMapping(path = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getAdmin() {
//        List<AdminInfoResponse> list = miyDimService.getAllAdmins();
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }

//    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getUsers() {
//        List<UserInfoResponse> list = miyDimService.getAllUsers();
//        return new ResponseEntity<>(list, HttpStatus.OK);
//    }

}
