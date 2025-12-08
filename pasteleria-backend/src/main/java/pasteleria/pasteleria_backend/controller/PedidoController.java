package pasteleria.pasteleria_backend.controller;

import java.util.List;
import java.util.Objects; 

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
import pasteleria.pasteleria_backend.model.Producto;
import pasteleria.pasteleria_backend.repository.PedidoRepository;
import pasteleria.pasteleria_backend.repository.ProductoRepository;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createPedido(@RequestBody Pedido pedido) {
        
        int subtotalCalculado = 0;
        // Bandera para Estrategia de Demanda Nivel 2 (Mayorista)
        boolean requiereConfirmacionStock = false;

        if (pedido.getProductos() != null) {
            for (DetallePedido detalle : pedido.getProductos()) {
                Producto productoReal = productoRepository.findById(detalle.getCodigo()).orElse(null);

                if (productoReal == null || (productoReal.getActivo() != null && !productoReal.getActivo())) {
                    return ResponseEntity.badRequest()
                        .body("Error: El producto '" + detalle.getNombre() + "' ya no está disponible.");
                }

                // --- ESTRATEGIA NIVEL 3: TECHO TÉCNICO / INDUSTRIAL ---
                // Protección contra desbordamiento y abuso de memoria
                if (detalle.getCantidad() > 1000) {
                    return ResponseEntity.badRequest()
                        .body("Error: La cantidad de '" + detalle.getNombre() + "' excede el límite técnico (1000). Contacte a ventas corporativas.");
                }

                // --- ESTRATEGIA NIVEL 2: VENTA MAYORISTA ---
                // Si supera 20 unidades, marcamos el pedido para revisión manual
                if (detalle.getCantidad() > 20) {
                    requiereConfirmacionStock = true;
                }

                // Fijamos el precio real de la BD (Anti-Hacker)
                detalle.setPrecio(productoReal.getPrecio());
                
                subtotalCalculado += (productoReal.getPrecio() * detalle.getCantidad());
            }
        }

        // --- SOLUCIÓN AL UNBOXING ---
        Integer descuentoFront = pedido.getDescuento();
        int descuentoSeguro = (descuentoFront != null) ? descuentoFront : 0;

        pedido.setSubtotal(subtotalCalculado);
        pedido.setDescuento(descuentoSeguro); 
        pedido.setTotal(Math.max(0, subtotalCalculado - descuentoSeguro));

        // Configuración final
        pedido.setId(null);
        
        // Lógica de Estado según Nivel de Demanda
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
            if (requiereConfirmacionStock) {
                pedido.setEstado("Por Confirmar Stock"); // Nivel 2
            } else {
                pedido.setEstado("Pendiente"); // Nivel 1
            }
        }

        if (pedido.getProductos() != null) {
            pedido.getProductos().forEach(p -> p.setListo(false));
        }
        
        Pedido nuevoPedido = pedidoRepository.save(pedido);
        return ResponseEntity.ok(nuevoPedido);
    }
    
    // ... (El resto del código: updatePedido, updateEstado, deletePedido se mantienen INTACTOS)
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable Long id, @RequestBody Pedido pedidoData) {
        System.out.println("🔄 Actualizando Pedido ID: " + id);

        return pedidoRepository.findById(id).map(pedidoExistente -> {
            
            if (pedidoData.getEstado() != null && !pedidoData.getEstado().equals("Sin Cambios")) {
                pedidoExistente.setEstado(pedidoData.getEstado());
            }
            
            if (pedidoExistente.getProductos() != null && pedidoData.getProductos() != null) {
                for (DetallePedido prodExistente : pedidoExistente.getProductos()) {
                    for (DetallePedido prodNuevo : pedidoData.getProductos()) {
                        if (Objects.equals(prodExistente.getId(), prodNuevo.getId())) {
                            prodExistente.setListo(prodNuevo.getListo());
                            break; 
                        }
                    }
                }
            }

            Pedido guardado = pedidoRepository.save(pedidoExistente);
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