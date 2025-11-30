package pasteleria.pasteleria_backend.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fechaEmision;
    private String horaEmision;
    private String fechaEntrega;

    @Embedded
    private ClienteData cliente;

    private Integer subtotal;
    private Integer descuento;
    private Integer total;
    private String estado; 

    // CAMBIO CRÍTICO: FetchType.EAGER
    // Esto obliga a cargar los productos SIEMPRE, evitando el error de LazyInitialization
    // y asegurando que el JSON llegue completo al React.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    private List<DetallePedido> productos;
}