package com.example.gestionacademica.repository;

import android.app.Application;

import com.example.gestionacademica.database.AppDatabase;
import com.example.gestionacademica.database.dao.EstudianteDao;
import com.example.gestionacademica.database.entities.Estudiante;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EstudianteRepository {
    private EstudianteDao estudianteDao;
    private ExecutorService executorService;

    public EstudianteRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        estudianteDao = database.estudianteDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Estudiante estudiante, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                long id = estudianteDao.insert(estudiante);
                if (listener != null) {
                    listener.onSuccess(id);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void update(Estudiante estudiante, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                estudianteDao.update(estudiante);
                if (listener != null) {
                    listener.onSuccess(0);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void delete(Estudiante estudiante, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                estudianteDao.delete(estudiante);
                if (listener != null) {
                    listener.onSuccess(0);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void getAllEstudiantes(OnDataLoadedListener<List<Estudiante>> listener) {
        executorService.execute(() -> {
            try {
                List<Estudiante> estudiantes = estudianteDao.getAllEstudiantes();
                if (listener != null) {
                    listener.onDataLoaded(estudiantes);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void getEstudianteById(int id, OnDataLoadedListener<Estudiante> listener) {
        executorService.execute(() -> {
            try {
                Estudiante estudiante = estudianteDao.getEstudianteById(id);
                if (listener != null) {
                    listener.onDataLoaded(estudiante);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void buscarEstudiantes(String busqueda, OnDataLoadedListener<List<Estudiante>> listener) {
        executorService.execute(() -> {
            try {
                List<Estudiante> estudiantes = estudianteDao.buscarEstudiantes(busqueda);
                if (listener != null) {
                    listener.onDataLoaded(estudiantes);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public interface OnOperationCompleteListener {
        void onSuccess(long id);
        void onError(String error);
    }

    public interface OnDataLoadedListener<T> {
        void onDataLoaded(T data);
        void onError(String error);
    }
}