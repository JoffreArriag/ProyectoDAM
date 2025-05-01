package com.example.login;

public class Cultivo {
    private String nombre;
    private String categoria;
    private String fechaInicio;
    private String ubicacion;

    public Cultivo(String nombre, String categoria, String fechaInicio, String ubicacion) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.fechaInicio = fechaInicio;
        this.ubicacion = ubicacion;
    }

    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getFechaInicio() { return fechaInicio; }
    public String getUbicacion() { return ubicacion; }
}

