package com.osbblevymista.api.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AdminInfoRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private Long adminId;

    @NotNull
    private boolean active;

}
