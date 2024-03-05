package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.Mapper;
import org.maxym.spring.sensor.dto.UserInfo;
import org.maxym.spring.sensor.model.User;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {

    UserInfo map(User user);
}
