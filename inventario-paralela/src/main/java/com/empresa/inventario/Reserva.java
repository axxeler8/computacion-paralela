package com.empresa.inventario;

import java.io.Serializable;

public class Reserva implements Serializable {
    private int idReserva;
    private int idVehiculo;
    private int sku;
    private int cantidad;

    public Reserva(int idReserva, int idVehiculo, int sku, int cantidad) {
        this.idReserva = idReserva;
        this.idVehiculo = idVehiculo;
        this.sku = sku;
        this.cantidad = cantidad;
    }
    public int getIdReserva() { return idReserva; }
    public int getIdVehiculo() { return idVehiculo; }
    public int getSku() { return sku; }
    public int getCantidad() { return cantidad; }
}