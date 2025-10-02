package com.example.gestionacademica.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gestionacademica.database.entities.Estudiante;
import com.example.gestionacademica.database.entities.Inscripcion;

import java.util.List;

public class EstudianteConInscripciones {
    @Embedded
    public Estudiante estudiante;

    @Relation(
            parentColumn = "id",
            entityColumn = "estudianteId"
    )
    public List<Inscripcion> inscripciones;
}