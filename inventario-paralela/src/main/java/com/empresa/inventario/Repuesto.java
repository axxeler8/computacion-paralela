package com.empresa.inventario;

import java.io.Serializable;

public class Repuesto implements Serializable {
    private int sku;
    private String nombre;
    private int cantidad;
    private int precio;
    private String categoria;      // ahora String
    private boolean disponible;

    public Repuesto(int sku, String nombre, int cantidad, int precio, String categoria, boolean disponible) {
        this.sku        = sku;
        this.nombre     = nombre;
        this.cantidad   = cantidad;
        this.precio     = precio;
        this.categoria  = categoria;
        this.disponible = disponible;
    }

    public int getSku()            { return sku; }
    public String getNombre()      { return nombre; }
    public int getCantidad()       { return cantidad; }
    public int getPrecio()         { return precio; }
    public String getCategoria()   { return categoria; }   // getter String
    public boolean isDisponible()  { return disponible; }
}
