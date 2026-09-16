package auth_service.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import auth_service.entity.User;


//Se elige MongoRepository porque en este modulo trabajamos por arquitectura por capas (bloqueante)
public interface UserRepository extends MongoRepository<User, String> {
 
    Optional<User> findByEmail(String email);
 
    boolean existsByEmail(String email);
}
 