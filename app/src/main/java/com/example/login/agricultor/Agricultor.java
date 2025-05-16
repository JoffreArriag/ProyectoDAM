package com.example.login.agricultor;

public class Agricultor {
    private String id;
    private String nombre;
    private int edad;
    private String zona;
    private String experiencia;


    public Agricultor() {}


    public Agricultor(String id, String nombre, int edad, String zona, String experiencia) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.zona = zona;
        this.experiencia = experiencia;
    }



    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
}
