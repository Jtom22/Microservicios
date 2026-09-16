package auth_service.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Aprovechamos que tienes Lombok en tu POM para ahorrar Getters/Setters
public abstract class Auditable {

    @CreatedDate // Spring mete la fecha actual automáticamente al insertar el registro
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // Spring actualiza esta fecha automáticamente al modificar el registro
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Field("created_by")
    private String createdBy; // Guarda el email o ID del usuario que creó el registro

    @LastModifiedBy
    @Field("updated_by")
    private String updatedBy; // Guarda el usuario que modificó el registro

    @Version // 👈 Crucial en MongoDB para el bloqueo optimista (evita colisiones de
             // escritura)
    private Long version;

}
