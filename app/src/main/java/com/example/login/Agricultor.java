package com.example.login;

public class Agricultor {
    private String nombre;
    private int edad;
    private String zona;
    private String experiencia;

    public Agricultor(String nombre, int edad, String zona, String experiencia) {
        this.nombre = nombre;
        this.edad = edad;
        this.zona = zona;
        this.experiencia = experiencia;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
}
