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

    private String codigo;   
    private String nombre;
    private Integer precio;
    private Integer cantidad;
    
    private String mensaje; // Para dedicatorias (Vi que lo usas en el frontend)
    
    // --- ESTO ES LO QUE FALTABA ---
    private Boolean listo;  // Para el checklist de cocina
    // -----------------------------
}