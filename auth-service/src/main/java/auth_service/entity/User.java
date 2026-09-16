package auth_service.entity;


import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;



@Data // Aprovechamos que tienes Lombok en tu POM para ahorrar Getters/Setters
@NoArgsConstructor
@Document(collection = "users") //Reemplaza a @Entity y @Table(name = "users")
@AllArgsConstructor
@Builder
public class User extends Auditable {

    @Id
    private String id;

    @Indexed(unique = true) // Crea un índice único en MongoDB para que no se repita
    private String email;

     @Field("first_name")
    private String firstName;

       @Field("last_name")
    private String lastName;

    @Field("password")
    private String password;



// Esto evita hacer un findByIdIn() o usar @DBRef cada vez que el usuario inicia sesión.
    @Field("roles")
    private Set<RoleType> roles = new HashSet<>(); 



}

