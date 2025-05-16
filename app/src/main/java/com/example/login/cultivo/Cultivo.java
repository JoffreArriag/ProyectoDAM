package com.example.login.cultivo;

public class Cultivo {
    private String idFirebase;
    private String nombre;
    private String categoria;
    private String fechaInicio;
    private String ubicacion;
    private double precioCaja;


    public Cultivo() {}


    public Cultivo(String nombre, String categoria, String fechaInicio, String ubicacion, double precioCaja) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.fechaInicio = fechaInicio;
        this.ubicacion = ubicacion;
        this.precioCaja = precioCaja;
    }

    // Getter y setter para idFirebase
    public String getIdFirebase() {
        return idFirebase;
    }
    public void setIdFirebase(String idFirebase) {
        this.idFirebase = idFirebase;
    }

    // Getters y setters para los otros campos
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public double getPrecioCaja() {
        return precioCaja;
    }
    public void setPrecioCaja(double precioCaja) {
        this.precioCaja = precioCaja;
    }
}

