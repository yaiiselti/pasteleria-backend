package pasteleria.pasteleria_backend.repository;

import java.util.List;
import java.util.Optional; // <--- Importamos Optional

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pasteleria.pasteleria_backend.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Para el historial "Mis Pedidos" (Usuario Logueado)
    List<Pedido> findByClienteEmail(String clienteEmail);

    // NUEVO: Para el Rastreador Público (Usuario Invitado)
    // Busca coincidencia EXACTA de ID y Email dentro del objeto embebido 'cliente'
    Optional<Pedido> findByIdAndCliente_Email(Long id, String email);


    // CORREGIDO: Usamos Email porque sabemos que "run" no existe en ClienteData
    List<Pedido> findByCliente_Email(String email);
}