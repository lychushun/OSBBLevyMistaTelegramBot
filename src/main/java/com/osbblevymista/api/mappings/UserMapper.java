package com.osbblevymista.api.mappings;

import com.opencsv.bean.CsvBindByName;
import com.osbblevymista.api.dto.response.UserInfoResponse;
import com.osbblevymista.telegram.models.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "chatId", source = "chatId")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "sentNotifications", source = "sentNotifications")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "createDateTs", source = "createDateTs")
    @Mapping(target = "lastActiveDateTs", source = "lastActiveDateTs")
    @Mapping(target = "apartment", source = "apartment")
    @Mapping(target = "house", source = "house")
    @Mapping(target = "userName", source = "userName")
    @Mapping(target = "firstNameMD", source = "firstNameMD")
    @Mapping(target = "lastNameMD", source = "lastNameMD")
    UserInfoResponse userInfoToUserInfoResponse(UserInfo userInfo);

}
