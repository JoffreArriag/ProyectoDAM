package com.example.login.tarea;

public class Tarea {
    private String id;
    private String nombre;
    private String descripcion;
    private String cultivo;
    private String fecha_inico;
    private String fecha_fin;
    private String agricultores;
    private String insumos;

    public Tarea() {}

    public Tarea(String id, String nombre, String descripcion, String cultivo, String fecha_inico, String fecha_fin, String agricultores, String insumos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cultivo = cultivo;
        this.fecha_inico = fecha_inico;
        this.fecha_fin = fecha_fin;
        this.agricultores = agricultores;
        this.insumos = insumos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgricultores() {
        return agricultores;
    }

    public void setAgricultores(String agricultores) {
        this.agricultores = agricultores;
    }

    public String getInsumos() {
        return insumos;
    }

    public void setInsumos(String insumos) {
        this.insumos = insumos;
    }

    public String getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(String fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getFecha_inico() {
        return fecha_inico;
    }

    public void setFecha_inico(String fecha_inico) {
        this.fecha_inico = fecha_inico;
    }

    public String getCultivo() {
        return cultivo;
    }

    public void setCultivo(String cultivo) {
        this.cultivo = cultivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}