package auth_service.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import auth_service.dto.UserRegisterDTO;
import auth_service.dto.UserResponseDTO;
import auth_service.entity.RoleType;
import auth_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    // El password llega en texto desde el DTO: se hashea en el Service,
    // NO en el mapper. Aquí lo ignoramos para no guardar texto plano por accidente.
    @Mapping(target = "password", ignore = true)
    // Los roles de negocio (ej. asignar ROLE_ADMIN) se deciden en el Service,
    // no vienen del registro publico del usuario.
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRegisterDTO dto);

    UserResponseDTO toResponseDTO(User user);

    default Set<String> mapRolesToStrings(Set<RoleType> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream().map(Enum::name).collect(Collectors.toSet());
    }
}