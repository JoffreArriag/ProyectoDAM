package com.example.login.mercado;

public class Venta {
    private String nombre;
    private String productos;
    private double total;

    public Venta() {

    }

    public Venta(String nombre, String productos, double total) {
        this.nombre = nombre;
        this.productos = productos;
        this.total = total;
    }

    public String getNombre() {
        return nombre;
    }

    public String getProductos() {
        return productos;
    }

    public double getTotal() {
        return total;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

