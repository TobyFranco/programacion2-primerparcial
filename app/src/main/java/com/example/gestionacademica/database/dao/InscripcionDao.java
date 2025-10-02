package com.example.gestionacademica.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.gestionacademica.database.entities.Inscripcion;
import com.example.gestionacademica.database.relations.InscripcionCompleta;

import java.util.List;

@Dao
public interface InscripcionDao {
    @Insert
    long insert(Inscripcion inscripcion);

    @Update
    void update(Inscripcion inscripcion);

    @Delete
    void delete(Inscripcion inscripcion);

    @Query("SELECT * FROM inscripciones ORDER BY fechaInscripcion DESC")
    List<Inscripcion> getAllInscripciones();

    @Query("SELECT * FROM inscripciones WHERE id = :id")
    Inscripcion getInscripcionById(int id);

    @Transaction
    @Query("SELECT * FROM inscripciones ORDER BY fechaInscripcion DESC")
    List<InscripcionCompleta> getInscripcionesCompletas();

    @Query("SELECT * FROM inscripciones WHERE estudianteId = :estudianteId")
    List<Inscripcion> getInscripcionesByEstudiante(int estudianteId);

    @Query("SELECT * FROM inscripciones WHERE materiaId = :materiaId")
    List<Inscripcion> getInscripcionesByMateria(int materiaId);
}