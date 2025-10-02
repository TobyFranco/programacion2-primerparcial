package com.example.gestionacademica.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gestionacademica.database.entities.Materia;

import java.util.List;

@Dao
public interface MateriaDao {
    @Insert
    long insert(Materia materia);

    @Update
    void update(Materia materia);

    @Delete
    void delete(Materia materia);

    @Query("SELECT * FROM materias ORDER BY nombre")
    List<Materia> getAllMaterias();

    @Query("SELECT * FROM materias WHERE id = :id")
    Materia getMateriaById(int id);

    @Query("SELECT * FROM materias WHERE codigo = :codigo")
    Materia getMateriaByCodigo(String codigo);

    @Query("SELECT * FROM materias WHERE nombre LIKE '%' || :busqueda || '%' OR codigo LIKE '%' || :busqueda || '%'")
    List<Materia> buscarMaterias(String busqueda);
}