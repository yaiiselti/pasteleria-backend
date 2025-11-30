package pasteleria.pasteleria_backend.controller;

import java.util.List;
import java.util.Objects; // Importante para comparaciones seguras

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pasteleria.pasteleria_backend.model.DetallePedido;
import pasteleria.pasteleria_backend.model.Pedido;
import pasteleria.pasteleria_backend.repository.DetallePedidoRepository;
import pasteleria.pasteleria_backend.repository.PedidoRepository;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        pedido.setId(null);
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
            pedido.setEstado("Pendiente");
        }
        if (pedido.getProductos() != null) {
            pedido.getProductos().forEach(p -> p.setListo(false));
        }
        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }
    
    // --- LÓGICA ROBUSTA CON LOGS DE DEPURACIÓN ---
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Long id, @RequestBody Pedido pedidoData) {
        System.out.println("🔄 Recibiendo actualización para Pedido ID: " + id);

        return pedidoRepository.findById(id).map(pedidoExistente -> {
            
            // 1. Actualizar Estado (Solo si viene uno válido y distinto a "Sin Cambios")
            if (pedidoData.getEstado() != null && !pedidoData.getEstado().equals("Sin Cambios")) {
                pedidoExistente.setEstado(pedidoData.getEstado());
            }
            
            // 2. Actualización Segura de Productos
            if (pedidoExistente.getProductos() != null && pedidoData.getProductos() != null) {
                
                System.out.println("   📦 Productos en BD: " + pedidoExistente.getProductos().size());
                System.out.println("   📨 Productos recibidos: " + pedidoData.getProductos().size());

                for (DetallePedido prodExistente : pedidoExistente.getProductos()) {
                    for (DetallePedido prodNuevo : pedidoData.getProductos()) {
                        
                        // DEBUG: Verificamos qué estamos comparando
                        // System.out.println("      Comparando BD[" + prodExistente.getId() + "] con Nuevo[" + prodNuevo.getId() + "]");

                        // COMPARACIÓN SEGURA DE IDs (Usando Objects.equals para evitar NullPointerException)
                        if (Objects.equals(prodExistente.getId(), prodNuevo.getId())) {
                            
                            System.out.println("      ✅ MATCH! Actualizando 'listo' a: " + prodNuevo.getListo());
                            prodExistente.setListo(prodNuevo.getListo());
                            break; 
                        }
                    }
                }
            }

            Pedido guardado = pedidoRepository.save(pedidoExistente);
            System.out.println("💾 Cambios guardados correctamente.");
            return ResponseEntity.ok(guardado);
            
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return pedidoRepository.findById(id).map(pedido -> {
            pedido.setEstado(nuevoEstado);
            pedidoRepository.save(pedido);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePedido(@PathVariable Long id) {
        if(pedidoRepository.existsById(id)){
            pedidoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}