package com.example.gestionacademica.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "estudiantes")
public class Estudiante {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private String apellido;
    private String carnet;
    private String email;
    private String telefono;

    // Constructor
    public Estudiante(String nombre, String apellido, String carnet, String email, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.carnet = carnet;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}