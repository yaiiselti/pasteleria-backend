package pasteleria.pasteleria_backend.model;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

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

    private Boolean activo;

    @ElementCollection
    @CollectionTable(name = "producto_imagenes", joinColumns = @JoinColumn(name = "producto_codigo"))
    @Column(name = "imagen_url")
    private List<String> imagenes;
}