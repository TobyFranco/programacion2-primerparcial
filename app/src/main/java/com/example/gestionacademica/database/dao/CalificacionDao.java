package com.example.gestionacademica.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gestionacademica.database.entities.Calificacion;

import java.util.List;

@Dao
public interface CalificacionDao {
    @Insert
    long insert(Calificacion calificacion);

    @Update
    void update(Calificacion calificacion);

    @Delete
    void delete(Calificacion calificacion);

    @Query("SELECT * FROM calificaciones ORDER BY fecha DESC")
    List<Calificacion> getAllCalificaciones();

    @Query("SELECT * FROM calificaciones WHERE id = :id")
    Calificacion getCalificacionById(int id);

    @Query("SELECT * FROM calificaciones WHERE inscripcionId = :inscripcionId ORDER BY fecha")
    List<Calificacion> getCalificacionesByInscripcion(int inscripcionId);

    @Query("SELECT AVG(nota * porcentaje / 100) FROM calificaciones WHERE inscripcionId = :inscripcionId")
    Double getPromedioByInscripcion(int inscripcionId);
}