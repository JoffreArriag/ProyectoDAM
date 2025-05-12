package com.example.login;

public class Agricultor {
    private int id;
    private String nombre;
    private int edad;
    private String zona;
    private String experiencia;

    // Constructor con ID (para leer desde la base de datos)
    public Agricultor(int id, String nombre, int edad, String zona, String experiencia) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.zona = zona;
        this.experiencia = experiencia;
    }

    // Constructor sin ID (para crear nuevos antes de insertar en la base de datos)
    public Agricultor(String nombre, int edad, String zona, String experiencia) {
        this.nombre = nombre;
        this.edad = edad;
        this.zona = zona;
        this.experiencia = experiencia;
    }

    // Getters y Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
}
