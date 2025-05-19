package com.empresa.inventario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.rmi.RemoteException;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:8100")
public class InventarioController {
    
    private final InventarioService inventarioService;

    public InventarioController() throws RemoteException {
        this.inventarioService = new InventarioServiceImpl();
    }

    // ==================== REPUESTOS ====================
    @GetMapping("/repuestos")
    public List<Repuesto> obtenerTodosRepuestos() throws RemoteException {
        return inventarioService.verRepuestos();
    }

    @GetMapping("/repuestos/{sku}")
    public Repuesto obtenerRepuestoPorSku(@PathVariable int sku) throws RemoteException {
        Repuesto repuesto = inventarioService.consultarRepuesto(sku);
        if(repuesto == null) throw new ResourceNotFoundException("Repuesto no encontrado");
        return repuesto;
    }

    @PostMapping("/repuestos")
    public void crearRepuesto(@RequestBody RepuestoRequest request) throws RemoteException {
        inventarioService.agregarRepuesto(
            request.getIdUbicacion(),
            request.getSku(),
            request.getCantidad(),
            request.getPrecio(),
            request.getCategoria(),
            request.isDisponible(),
            request.getNombre()
        );
    }

    @PutMapping("/repuestos/{idUbicacion}/{sku}/liberar")
    public void liberarStock(
        @PathVariable int idUbicacion,
        @PathVariable int sku,
        @RequestParam int cantidad
    ) throws RemoteException {
        inventarioService.liberarRepuesto(idUbicacion, sku, cantidad);
    }

    // ==================== RESERVAS ====================
    @GetMapping("/reservas")
    public List<Reserva> obtenerTodasReservas() throws RemoteException {
        return inventarioService.verReservas();
    }

    @GetMapping("/reservas/{id}")
    public Reserva obtenerReservaPorId(@PathVariable int id) throws RemoteException {
        Reserva reserva = inventarioService.consultarReserva(id);
        if(reserva == null) throw new ResourceNotFoundException("Reserva no encontrada");
        return reserva;
    }

    @PostMapping("/reservas")
    public void crearReserva(@RequestBody ReservaRequest request) throws RemoteException {
        inventarioService.agregarReserva(
            request.getIdVehiculo(),
            request.getSku(),
            request.getCantidad()
        );
    }

    @DeleteMapping("/reservas/{id}")
    public void eliminarReserva(@PathVariable int id) throws RemoteException {
        inventarioService.liberarReserva(id);
    }

    // ==================== UBICACIONES ====================
    @GetMapping("/ubicaciones/{id}")
    public Ubicacion obtenerUbicacion(@PathVariable int id) throws RemoteException {
        Ubicacion ubicacion = inventarioService.consultarUbicacion(id);
        if(ubicacion == null) throw new ResourceNotFoundException("Ubicación no encontrada");
        return ubicacion;
    }

    @GetMapping("/ubicaciones/{id}/stock")
    public int consultarStockUbicacion(@PathVariable int id) throws RemoteException {
        return inventarioService.consultarStockUbicacion(id);
    }

    // ==================== VEHÍCULOS ====================
    @GetMapping("/vehiculos/{id}")
    public Vehiculo obtenerVehiculo(@PathVariable int id) throws RemoteException {
        Vehiculo vehiculo = inventarioService.consultarVehiculo(id);
        if(vehiculo == null) throw new ResourceNotFoundException("Vehículo no encontrado");
        return vehiculo;
    }

    public static class RepuestoRequest {
        private int idUbicacion;
        private int sku;
        private int cantidad;
        private int precio;
        private String categoria;
        private boolean disponible;
        private String nombre;

        // Getters y Setters
        public int getIdUbicacion() { return idUbicacion; }
        public void setIdUbicacion(int idUbicacion) { this.idUbicacion = idUbicacion; }

        public int getSku() { return sku; }
        public void setSku(int sku) { this.sku = sku; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public int getPrecio() { return precio; }
        public void setPrecio(int precio) { this.precio = precio; }

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        public boolean isDisponible() { return disponible; }
        public void setDisponible(boolean disponible) { this.disponible = disponible; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    public static class ReservaRequest {
        private int idVehiculo;
        private int sku;
        private int cantidad;

        // Getters y Setters
        public int getIdVehiculo() { return idVehiculo; }
        public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }

        public int getSku() { return sku; }
        public void setSku(int sku) { this.sku = sku; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }

    // ==================== MANEJO DE ERRORES ====================
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Recurso no encontrado")
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(RemoteException.class)
    public ResponseEntity<String> handleRemoteException(RemoteException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body("Error de conexión con el servidor RMI: " + ex.getMessage());
    }
}