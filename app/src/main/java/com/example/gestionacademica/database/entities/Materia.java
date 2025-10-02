package com.example.gestionacademica.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "materias")
public class Materia {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String codigo;
    private String nombre;
    private int creditos;
    private String descripcion;

    public Materia(String codigo, String nombre, int creditos, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.creditos = creditos;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}