package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.*;
import org.maxym.spring.sensor.dto.UserRequestDto;
import org.maxym.spring.sensor.dto.UserResponseDto;
import org.maxym.spring.sensor.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User map(UserRequestDto userRequestDTO);

    @Named("map")
    @InheritInverseConfiguration
    UserResponseDto map(User user);

    @IterableMapping(qualifiedByName = "map")
    List<UserResponseDto> mapList(List<User> users);
}
