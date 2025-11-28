package pasteleria.pasteleria_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalles_pedido")
@Data
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productoCodigo;
    private String nombre; // Guardamos el nombre por si el producto se borra después
    private Integer precio;
    private Integer cantidad;

    // IMPORTANTE: No pongas aquí una referencia completa al objeto Pedido (@ManyToOne) 
    // a menos que uses @JsonIgnore, o Spring entrará en un bucle infinito.
    // Para este proyecto simple, no es necesario enlazarlo de vuelta.
}