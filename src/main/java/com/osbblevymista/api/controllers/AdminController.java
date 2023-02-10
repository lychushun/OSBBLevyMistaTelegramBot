package com.osbblevymista.api.controllers;


import com.osbblevymista.api.dto.request.AdminInfoRequest;
import com.osbblevymista.api.dto.response.AdminInfoResponse;
import com.osbblevymista.api.services.MiyDimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final MiyDimService miyDimService;

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addAdmin(@RequestBody AdminInfoRequest adminInfoRequest) {
        Boolean res = miyDimService.addAdmin(adminInfoRequest);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping(path = "{adminId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteAdmin(@PathVariable("adminId") Long adminAd) {
        Boolean res = miyDimService.deleteAdmin(adminAd);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAdmin() {
        List<AdminInfoResponse> list = miyDimService.getAllAdmins();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


}
