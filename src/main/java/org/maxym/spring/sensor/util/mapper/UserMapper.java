package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.maxym.spring.sensor.dto.UserRequestDTO;
import org.maxym.spring.sensor.dto.UserResponseDTO;
import org.maxym.spring.sensor.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "email", target = "email")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    UserResponseDTO userToUserResponseDTO(User user);
}
