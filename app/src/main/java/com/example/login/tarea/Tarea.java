package com.example.login.tarea;

public class Tarea {

    private String nombre;
    private String descripcion;
    private String cultivo;
    private String fecha_inico;
    private String fecha_fin;

    public Tarea(String nombre, String descripcion, String cultivo, String fecha_inico, String fecha_fin) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cultivo = cultivo;
        this.fecha_inico = fecha_inico;
        this.fecha_fin = fecha_fin;
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
