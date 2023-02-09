package com.osbblevymista.api.mappings;

import com.osbblevymista.api.dto.request.AdminInfoRequest;
import com.osbblevymista.api.dto.response.AdminInfoResponse;
import com.osbblevymista.telegram.models.AdminInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(target = "active", source = "active")
    @Mapping(target = "adminId", source = "adminId")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "firstName", source = "firstName")
    AdminInfoResponse adminInfoToAdminInfoResponse(AdminInfo adminInfo);

    @Mapping(target = "active", source = "active")
    @Mapping(target = "adminId", source = "adminId")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "firstName", source = "firstName")
    AdminInfo adminInfoResponseToAdminInfo(AdminInfoRequest adminInfoResponse);

}
