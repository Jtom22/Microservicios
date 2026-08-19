package auth_service.services;

import java.util.List;

import auth_service.dto.UserRegisterDTO;
import auth_service.dto.UserResponseDTO;
import auth_service.entity.User;

public interface UserService {
 
    UserResponseDTO register(UserRegisterDTO dto);
 
    UserResponseDTO findByEmail(String email);

    List<UserResponseDTO> findAll();

    //UserResponseDTO login(UserRegisterDTO dto);
}
 
