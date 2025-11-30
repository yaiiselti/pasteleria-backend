// En pasteleria.pasteleria_backend.repository.ResenaRepository.java
package pasteleria.pasteleria_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pasteleria.pasteleria_backend.model.Resena;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByCodigoProducto(String codigoProducto);
}