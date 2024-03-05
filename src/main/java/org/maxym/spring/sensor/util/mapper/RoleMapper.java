package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.Mapper;
import org.maxym.spring.sensor.dto.RoleResponse;
import org.maxym.spring.sensor.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse map(Role role);
}
