package com.osbblevymista.api.mappings;

import com.osbblevymista.api.dto.request.AdminInfoRequest;
import com.osbblevymista.api.dto.response.AdminInfoResponse;
import com.osbblevymista.telegram.models.AdminInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    AdminInfoResponse adminInfoToAdminInfoResponse(AdminInfo adminInfo);

    AdminInfo adminInfoResponseToAdminInfo(AdminInfoRequest adminInfoResponse);

}
