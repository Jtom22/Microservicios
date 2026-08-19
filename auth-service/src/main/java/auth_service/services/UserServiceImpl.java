package auth_service.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import auth_service.dto.UserRegisterDTO;
import auth_service.dto.UserResponseDTO;
import auth_service.entity.RoleType;
import auth_service.entity.User;
import auth_service.exceptions.EmailAlreadyExistsException;
import auth_service.mapper.UserMapper;
import auth_service.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO register(UserRegisterDTO dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new EmailAlreadyExistsException("El email " + dto.email() + "ya existe"));

        userMapper.toEntity(dto);

        user.builder();
        // Rol por defecto: el registro público no puede ser un ROLE_ADMIN.
        user.setRoles(Set.of(RoleType.ROLE_USER));

        User saved = userRepository.save(user);
        return userMapper.toResponseDTO(saved);
    }
    

    @Override
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + email));
        return userMapper.toResponseDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDTO> findAll() {
        //user.getRoles().stream().map(String::valueOf).collect(Collectors.toSet()) -> sacamos el set desde los enum de RoleType
        //Con el String::valueOf sacamos el valor del texto del enum sin warning por null
           return userRepository.findAll().stream()
            .map(user -> new UserResponseDTO(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(),user.getRoles().stream().map(String::valueOf).collect(Collectors.toSet()), user.getCreatedAt()))
            .toList(); // ¡Aquí aplicamos los Streams que vimos al inicio!

    }

//     @Override
//     public UserResponseDto login(UserRegisterDTO dto) {
//         // TODO Auto-generated method stub
//         UserResponseDTO dtoLogin=userMapper.toResponseDTO(null);
//             userRepository.existsByEmail(dto.email())
//             if (userRepository.existsByEmail(dto.email())) {
//                 dtoLogin=userMapper.toResponseDTO(dto);
//                 return dtoLogin;
                
//                     }
            

//         return dtoLogin;
//     }
}