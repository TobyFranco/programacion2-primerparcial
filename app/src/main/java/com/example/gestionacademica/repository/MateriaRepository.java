package com.example.gestionacademica.repository;

import android.app.Application;

import com.example.gestionacademica.database.AppDatabase;
import com.example.gestionacademica.database.dao.MateriaDao;
import com.example.gestionacademica.database.entities.Materia;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MateriaRepository {
    private MateriaDao materiaDao;
    private ExecutorService executorService;

    public MateriaRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        materiaDao = database.materiaDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Materia materia, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                long id = materiaDao.insert(materia);
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

    public void update(Materia materia, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                materiaDao.update(materia);
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

    public void delete(Materia materia, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                materiaDao.delete(materia);
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

    public void getAllMaterias(OnDataLoadedListener<List<Materia>> listener) {
        executorService.execute(() -> {
            try {
                List<Materia> materias = materiaDao.getAllMaterias();
                if (listener != null) {
                    listener.onDataLoaded(materias);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public void getMateriaById(int id, OnDataLoadedListener<Materia> listener) {
        executorService.execute(() -> {
            try {
                Materia materia = materiaDao.getMateriaById(id);
                if (listener != null) {
                    listener.onDataLoaded(materia);
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