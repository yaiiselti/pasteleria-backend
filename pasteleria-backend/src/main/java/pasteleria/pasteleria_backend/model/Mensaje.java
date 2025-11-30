package pasteleria.pasteleria_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "mensajes")
@Data
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String comentario;
    
    // Lo manejamos como String para facilitar la compatibilidad con el frontend
    // o LocalDateTime si prefieres, pero el frontend manda strings a veces.
    // Para simplificar auditoría, lo dejamos compatible con lo que envíe React.
    private String fecha; 

    // CAMBIO: Campo nuevo requerido por ContactoService.ts
    private Boolean leido = false; 
}