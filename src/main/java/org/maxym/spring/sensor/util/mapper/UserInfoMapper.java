package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.Mapper;
import org.maxym.spring.sensor.dto.UserInfoDTO;
import org.maxym.spring.sensor.model.User;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {

    UserInfoDTO map(User user);
}
