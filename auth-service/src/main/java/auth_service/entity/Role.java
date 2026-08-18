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
@Document(collection = "roles") //Reemplaza a @Entity y @Table(name = "roles")
@AllArgsConstructor
@Builder
public class Role {


    @Id
    private Long id;

    @Indexed(unique = true) // Evita que se repita el rol en la base de datos
    private RoleType name; // Ej: "ROLE_ADMIN"

    @Field("role_ids")
    private Set<String> roleIds = new HashSet<>();

}
