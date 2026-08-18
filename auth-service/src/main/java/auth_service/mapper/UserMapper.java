package auth_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import auth_service.dto.UserRegisterDTO;
import auth_service.dto.UserResponseDTO;
import auth_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true) // Esta línea (usa el nombre exacto del campo en tu clase User)
    User toEntity(UserRegisterDTO dto);


    // Ignoramos 'roles' porque no existe en la entidad User (ahí se llama roleIds)
    // y lo rellenaremos a mano en la capa de Servicio con los textos reales.
    @Mapping(target = "roles", ignore = true)
    UserResponseDTO toResponseDTO(User user);

}