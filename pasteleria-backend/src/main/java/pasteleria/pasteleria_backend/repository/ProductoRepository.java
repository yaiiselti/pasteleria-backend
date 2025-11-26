package pasteleria.pasteleria_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pasteleria.pasteleria_backend.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    // Método mágico: Spring crea el SQL solo con ver el nombre
    List<String> findByCategoria(String categoria);
}