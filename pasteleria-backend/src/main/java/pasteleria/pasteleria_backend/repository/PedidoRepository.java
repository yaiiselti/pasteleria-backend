package pasteleria.pasteleria_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pasteleria.pasteleria_backend.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Para el historial "Mis Pedidos" del usuario
    List<Pedido> findByClienteEmail(String clienteEmail);
}