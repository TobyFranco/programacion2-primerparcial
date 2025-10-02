package com.example.gestionacademica.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "inscripciones",
        foreignKeys = {
                @ForeignKey(entity = Estudiante.class,
                        parentColumns = "id",
                        childColumns = "estudianteId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Materia.class,
                        parentColumns = "id",
                        childColumns = "materiaId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("estudianteId"), @Index("materiaId")})
public class Inscripcion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int estudianteId;
    private int materiaId;
    private String semestre;
    private String anio;
    private String fechaInscripcion;

    public Inscripcion(int estudianteId, int materiaId, String semestre, String anio, String fechaInscripcion) {
        this.estudianteId = estudianteId;
        this.materiaId = materiaId;
        this.semestre = semestre;
        this.anio = anio;
        this.fechaInscripcion = fechaInscripcion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEstudianteId() { return estudianteId; }
    public void setEstudianteId(int estudianteId) { this.estudianteId = estudianteId; }

    public int getMateriaId() { return materiaId; }
    public void setMateriaId(int materiaId) { this.materiaId = materiaId; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public String getAnio() { return anio; }
    public void setAnio(String anio) { this.anio = anio; }

    public String getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(String fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
}