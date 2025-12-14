package pasteleria.pasteleria_backend.model;

public enum EstadoPedido {
    POR_CONFIRMAR_STOCK, // 1. Nuevo estado inicial
    PENDIENTE,           // 2. Esperando pago o validación
    EN_PREPARACION,      // 3. En cocina
    EN_REPARTO,          // 4. Salió a despacho (Nuevo)
    ENTREGADO,           // 5. Finalizado
    CANCELADO            // 6. Rechazado
}