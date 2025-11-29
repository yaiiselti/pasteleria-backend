package pasteleria.pasteleria_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.model.Pedido;
import pasteleria.pasteleria_backend.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @GetMapping("/mis-pedidos")
    public List<Pedido> getMisPedidos(@RequestParam String email) {
        return pedidoService.getPedidosByCliente(email);
    }

    @PostMapping
    public Pedido createPedido(@RequestBody Pedido pedido) {
        return pedidoService.savePedido(pedido);
    }

    // --- ACTUALIZAR PEDIDO (Para el checklist de cocina) ---
    @PutMapping("/{id}")
    public Pedido updatePedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        // Al guardar con el mismo ID, Spring actualiza los datos (incluyendo el estado "listo" de los productos)
        pedido.setId(id); 
        return pedidoService.savePedido(pedido);
    }

    // --- CAMBIAR SOLO ESTADO (Rápido) ---
    @PutMapping("/{id}/estado")
    public Pedido updateEstadoPedido(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return pedidoService.updateEstado(id, nuevoEstado);
    }

    // --- ELIMINAR PEDIDO (Faltaba esto) ---
    @DeleteMapping("/{id}")
    public void deletePedido(@PathVariable Long id) {
        pedidoService.deletePedido(id);
    }

}