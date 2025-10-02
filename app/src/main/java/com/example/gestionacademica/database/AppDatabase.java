package com.example.gestionacademica.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gestionacademica.database.dao.CalificacionDao;
import com.example.gestionacademica.database.dao.EstudianteDao;
import com.example.gestionacademica.database.dao.InscripcionDao;
import com.example.gestionacademica.database.dao.MateriaDao;
import com.example.gestionacademica.database.entities.Calificacion;
import com.example.gestionacademica.database.entities.Estudiante;
import com.example.gestionacademica.database.entities.Inscripcion;
import com.example.gestionacademica.database.entities.Materia;

@Database(entities = {Estudiante.class, Materia.class, Inscripcion.class, Calificacion.class},
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract EstudianteDao estudianteDao();
    public abstract MateriaDao materiaDao();
    public abstract InscripcionDao inscripcionDao();
    public abstract CalificacionDao calificacionDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "gestion_academica_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}