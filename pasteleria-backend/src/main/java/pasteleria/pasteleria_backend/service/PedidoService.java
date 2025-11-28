package pasteleria.pasteleria_backend.service;

import pasteleria.pasteleria_backend.model.Pedido;
import pasteleria.pasteleria_backend.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }
    
    public List<Pedido> getPedidosByCliente(String email) {
        return pedidoRepository.findByClienteEmail(email);
    }

    public Pedido savePedido(Pedido pedido) {
        // Estado inicial por defecto si no viene
        if (pedido.getEstado() == null) {
            pedido.setEstado("Pendiente");
        }
        return pedidoRepository.save(pedido);
    }

    // Lógica para cambiar estado (usado por el Admin)
    public Pedido updateEstado(Long id, String nuevoEstado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        }
        return null;
    }

    public void deletePedido(Long id) {
        pedidoRepository.deleteById(id);
    }
}