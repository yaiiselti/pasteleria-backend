package pasteleria.pasteleria_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "detalles_pedido")
@Getter @Setter // CAMBIO: Usamos esto en lugar de @Data para seguridad en JPA
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;   
    private String nombre;
    private Integer precio;
    private Integer cantidad;
    
    private String mensaje;
    
    private Boolean listo;
    
    // Constructor vacío necesario para JPA
    public DetallePedido() {}
}