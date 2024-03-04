package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.maxym.spring.sensor.dto.UserRequestDTO;
import org.maxym.spring.sensor.dto.UserResponseDTO;
import org.maxym.spring.sensor.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);

    UserResponseDTO userToUserResponseDTO(User user);
}
