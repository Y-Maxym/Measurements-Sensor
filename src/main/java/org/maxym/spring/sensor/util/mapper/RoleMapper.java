package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.maxym.spring.sensor.dto.RoleResponse;
import org.maxym.spring.sensor.model.Role;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Named("map")
    RoleResponse map(Role role);

    @IterableMapping(qualifiedByName = "map")
    List<RoleResponse> mapList(List<Role> roles);
}
