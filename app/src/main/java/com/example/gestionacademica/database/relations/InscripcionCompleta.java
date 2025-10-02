package com.example.gestionacademica.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.gestionacademica.database.entities.Estudiante;
import com.example.gestionacademica.database.entities.Inscripcion;
import com.example.gestionacademica.database.entities.Materia;

public class InscripcionCompleta {
    @Embedded
    public Inscripcion inscripcion;

    @Relation(
            parentColumn = "estudianteId",
            entityColumn = "id"
    )
    public Estudiante estudiante;

    @Relation(
            parentColumn = "materiaId",
            entityColumn = "id"
    )
    public Materia materia;
}