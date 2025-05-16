package com.example.login;

public class Usuario {
    private String cedula;
    private String nombres;
    private String apellidos;
    private String edad;
    private String nacionalidad;
    private String genero;
    private String estadoCivil;
    private String fechaNacimiento;
    private float ratingIngles;


    public Usuario() {}


    public Usuario(String cedula, String nombres, String apellidos, String edad,
                   String nacionalidad, String genero, String estadoCivil,
                   String fechaNacimiento, float ratingIngles) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.genero = genero;
        this.estadoCivil = estadoCivil;
        this.fechaNacimiento = fechaNacimiento;
        this.ratingIngles = ratingIngles;
    }


    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public float getRatingIngles() {
        return ratingIngles;
    }

    public void setRatingIngles(float ratingIngles) {
        this.ratingIngles = ratingIngles;
    }
}
