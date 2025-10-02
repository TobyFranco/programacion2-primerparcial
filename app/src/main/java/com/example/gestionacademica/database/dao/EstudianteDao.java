package com.example.gestionacademica.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gestionacademica.database.entities.Estudiante;
import com.example.gestionacademica.database.relations.EstudianteConInscripciones;

import java.util.List;

@Dao
public interface EstudianteDao {
    @Insert
    long insert(Estudiante estudiante);

    @Update
    void update(Estudiante estudiante);

    @Delete
    void delete(Estudiante estudiante);

    @Query("SELECT * FROM estudiantes ORDER BY apellido, nombre")
    List<Estudiante> getAllEstudiantes();

    @Query("SELECT * FROM estudiantes WHERE id = :id")
    Estudiante getEstudianteById(int id);

    @Query("SELECT * FROM estudiantes WHERE carnet = :carnet")
    Estudiante getEstudianteByCarnet(String carnet);

    @Query("SELECT * FROM estudiantes WHERE nombre LIKE '%' || :busqueda || '%' OR apellido LIKE '%' || :busqueda || '%' OR carnet LIKE '%' || :busqueda || '%'")
    List<Estudiante> buscarEstudiantes(String busqueda);

    @Query("SELECT * FROM estudiantes")
    List<EstudianteConInscripciones> getEstudiantesConInscripciones();
}