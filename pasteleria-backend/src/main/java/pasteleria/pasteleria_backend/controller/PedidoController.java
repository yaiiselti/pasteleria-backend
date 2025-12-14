package pasteleria.pasteleria_backend.controller;

import java.util.List;
import java.util.Objects; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import pasteleria.pasteleria_backend.service.PedidoService;
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createPedido(@RequestBody Pedido pedido) {
        
        // --- ESTRATEGIA DE DOBLE CANDADO: VALIDACIÓN GLOBAL (Inicio) ---
        // 1. Cálculo del volumen total antes de procesar lógica de negocio.
        int totalGlobalUnidades = 0;
        if (pedido.getProductos() != null) {
            for (DetallePedido dp : pedido.getProductos()) {
                totalGlobalUnidades += dp.getCantidad();
            }
        }

        // 2. CANDADO DE SEGURIDAD (Nivel 3 - Industrial)
        // Bloqueo absoluto si la suma total excede la capacidad técnica.
        if (totalGlobalUnidades > 1000) {
            return ResponseEntity.badRequest()
                .body("Error de Seguridad: La cantidad total del pedido (" + totalGlobalUnidades + ") excede el límite logístico permitido (1000).");
        }
        // --- FIN ESTRATEGIA DOBLE CANDADO ---

        int subtotalCalculado = 0;
        
        // Bandera para Estrategia de Demanda Nivel 2 (Mayorista)
        // REGLA DE NEGOCIO ACTUALIZADA: Si el total global > 30, pasa a revisión.
        boolean requiereConfirmacionStock = (totalGlobalUnidades > 30);

        if (pedido.getProductos() != null) {
            for (DetallePedido detalle : pedido.getProductos()) {
                Producto productoReal = productoRepository.findById(detalle.getCodigo()).orElse(null);

                if (productoReal == null || (productoReal.getActivo() != null && !productoReal.getActivo())) {
                    return ResponseEntity.badRequest()
                        .body("Error: El producto '" + detalle.getNombre() + "' ya no está disponible.");
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
        
        // Lógica de Estado según Nivel de Demanda (La Verdad Absoluta)
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
            if (requiereConfirmacionStock) {
                // Nivel 2: Mayorista (31 - 1000 unidades)
                pedido.setEstado("Por Confirmar Stock"); 
            } else {
                // Nivel 1: Retail (0 - 30 unidades)
                pedido.setEstado("Pendiente"); 
            }
        }

        if (pedido.getProductos() != null) {
            pedido.getProductos().forEach(p -> p.setListo(false));
        }
        
        Pedido nuevoPedido = pedidoRepository.save(pedido);
        return ResponseEntity.ok(nuevoPedido);
    }
    
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
    
    @GetMapping("/{id}/track")
    public ResponseEntity<?> trackearPedido(@PathVariable Long id, @RequestParam String email) {
        // Ahora sí reconocerá "pedidoService"
        Pedido pedido = pedidoService.findById(id); 

    if (pedido == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("El pedido no existe.");
    }

    // 2. SEGURIDAD: Verificamos que el email coincida con el del pedido
    // Esto evita que cualquiera vea pedidos ajenos adivinando el número
    if (!pedido.getCliente().getEmail().equalsIgnoreCase(email)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN) // 403
            .body("El correo no coincide con el registrado en este pedido.");
    }

    // 3. Si todo coincide, devolvemos el pedido
    return ResponseEntity.ok(pedido);
    }

    // CORREGIDO: Usamos @RequestParam para evitar que Spring corte el ".com"
    @GetMapping("/cliente") // <--- Ya NO ponemos /{email} aquí
    public ResponseEntity<List<Pedido>> getPedidosByCliente(@RequestParam String email) { // <--- Cambiamos a @RequestParam
        // Ahora el email llega completo (ej: usuario@gmail.com)
        List<Pedido> pedidos = pedidoRepository.findByCliente_Email(email);
        return ResponseEntity.ok(pedidos);
    }
}