package pasteleria.pasteleria_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "detalles_pedido")
@Data
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;   // Código del pastel
    private String nombre;
    private Integer precio;
    private Integer cantidad;
    
    // Relación inversa (opcional pero útil): Saber a qué pedido pertenece este detalle
    // @ManyToOne
    // @JoinColumn(name = "pedido_id")
    // private Pedido pedido;
}