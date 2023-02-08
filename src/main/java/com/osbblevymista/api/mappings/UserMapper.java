package com.osbblevymista.api.mappings;

import com.osbblevymista.api.dto.response.UserInfoResponse;
import com.osbblevymista.telegram.models.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoResponse userInfoToUserInfoResponse(UserInfo userInfo);

}
