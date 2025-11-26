package pasteleria.pasteleria_backend.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID numérico (ej: 1005)
    private Long id;

    @Column(name = "fecha_emision")
    private String fechaEmision;
    
    @Column(name = "hora_emision")
    private String horaEmision;
    
    @Column(name = "fecha_entrega")
    private String fechaEntrega;

    private String clienteNombre;
    private String clienteEmail;
    private String clienteDireccion;
    private String clienteComuna;
    private String medioPago;

    private Integer total;
    private String estado; 
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id") // Esto crea la llave foránea en la tabla detalles
    private List<DetallePedido> productos;
}