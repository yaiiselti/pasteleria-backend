package pasteleria.pasteleria_backend.model;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "productos")
@Data 
public class Producto {

    @Id
    @Column(length = 20)
    private String codigo; // Ej: "TC001" - Usamos esto como ID

    private String nombre;

    private String categoria;

    private Integer precio;

    @Column(length = 1000) // Permitimos descripciones largas
    private String descripcion;

    @ElementCollection
    @CollectionTable(name = "producto_imagenes", joinColumns = @JoinColumn(name = "producto_codigo"))
    @Column(name = "imagen_url")
    private List<String> imagenes;
}