package pasteleria.pasteleria_backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pasteleria.pasteleria_backend.model.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Buscar todas las reseñas de un producto "TC001"
    List<Resena> findByCodigoProducto(String codigoProducto);
}