package com.example.odisea.data;

public class Lugar {
    private String id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private float calificacion;
    private String imagenUrl;
    private String tipoEstablecimiento; // "hotel", "spa", "restaurante", "pista"

    public Lugar(String id, String nombre, String descripcion, String ubicacion, float calificacion, String imagenUrl, String tipoEstablecimiento) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.calificacion = calificacion;
        this.imagenUrl = imagenUrl;
        this.tipoEstablecimiento = tipoEstablecimiento;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getUbicacion() { return ubicacion; }
    public float getCalificacion() { return calificacion; }
    public String getImagenUrl() { return imagenUrl; }
    public String getTipoEstablecimiento() { return tipoEstablecimiento; }
}