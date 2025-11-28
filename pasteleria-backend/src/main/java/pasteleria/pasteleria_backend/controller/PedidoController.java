package pasteleria.pasteleria_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/pedidos") // La ruta base para todo lo de pedidos
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // 1. GET: Admin ve todos los pedidos
    // http://localhost:8085/api/pedidos
    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    // 2. GET: Cliente ve SOLO sus pedidos (Filtramos por email)
    // http://localhost:8085/api/pedidos/mis-pedidos?email=juan@correo.cl
    @GetMapping("/mis-pedidos")
    public List<Pedido> getMisPedidos(@RequestParam String email) {
        return pedidoService.getPedidosByCliente(email);
    }

    // 3. POST: Crear un nuevo pedido (Cuando le dan "Pagar" en React)
    // http://localhost:8085/api/pedidos
    @PostMapping
    public Pedido createPedido(@RequestBody Pedido pedido) {
        return pedidoService.savePedido(pedido);
    }

    // 4. PUT: Admin actualiza estado (Pendiente -> Entregado)
    // http://localhost:8085/api/pedidos/1/estado?nuevoEstado=Entregado
    @PutMapping("/{id}/estado")
    public Pedido updateEstadoPedido(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return pedidoService.updateEstado(id, nuevoEstado);
    }
}