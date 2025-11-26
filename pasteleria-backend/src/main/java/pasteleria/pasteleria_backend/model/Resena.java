package pasteleria.pasteleria_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "resenas")
@Data
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID Autoincrementable (1, 2, 3...)
    private Long id;

    @Column(name = "codigo_producto")
    private String codigoProducto; // "TC001"

    @Column(name = "email_usuario")
    private String emailUsuario;   // "juan@gmail.com"

    @Column(name = "nombre_usuario")
    private String nombreUsuario;

    private Integer calificacion;  // 1-5
    private String comentario;
    private String fecha;          // Guardamos la fecha como texto por simplicidad
}
