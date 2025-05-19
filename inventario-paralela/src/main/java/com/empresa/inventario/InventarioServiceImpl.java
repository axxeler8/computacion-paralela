package com.empresa.inventario;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class InventarioServiceImpl extends UnicastRemoteObject implements InventarioService {
    public InventarioServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<Repuesto> verRepuestos() throws RemoteException {
        return Database.obtenerTodosRepuestos();
    }

    @Override
    public Repuesto consultarRepuesto(int sku) throws RemoteException {
        return Database.obtenerRepuestoPorSku(sku);
    }

    @Override
    public void agregarRepuesto(int idUbicacion, int sku, int cantidad, int precio, String categoria, boolean disponible, String nombre) throws RemoteException {
        Database.insertarRepuesto(idUbicacion, sku, cantidad, precio, categoria, disponible, nombre);
    }

    @Override
    public void liberarRepuesto(int idUbicacion, int sku, int cantidad) throws RemoteException {
        Database.actualizarStock(idUbicacion, sku, -cantidad);
    }

    @Override
    public List<Reserva> verReservas() throws RemoteException {
        return Database.obtenerTodasReservas();
    }

    @Override
    public Reserva consultarReserva(int idReserva) throws RemoteException {
        return Database.obtenerReservaPorId(idReserva);
    }

    @Override
    public void agregarReserva(int idVehiculo, int sku, int cantidad) throws RemoteException {
        Database.insertarReserva(idVehiculo, sku, cantidad);
    }

    @Override
    public void liberarReserva(int idReserva) throws RemoteException {
        Database.eliminarReserva(idReserva);
    }

    @Override
    public Ubicacion consultarUbicacion(int idUbicacion) throws RemoteException {
        return Database.obtenerUbicacionPorId(idUbicacion);
    }

    @Override
    public int consultarStockUbicacion(int idUbicacion) throws RemoteException {
        return Database.obtenerStockTotalPorUbicacion(idUbicacion);
    }

    @Override
    public Vehiculo consultarVehiculo(int idVehiculo) throws RemoteException {
        return Database.obtenerVehiculoPorId(idVehiculo);
    }

    @Override
    public Repuesto consultarRepuestoEnUbicacion(int idUbicacion, int sku) throws RemoteException {
        return Database.obtenerRepuestoPorUbicacion(idUbicacion, sku);
    }
}
