package com.example.gestionacademica.repository;

import android.app.Application;

import com.example.gestionacademica.database.AppDatabase;
import com.example.gestionacademica.database.dao.InscripcionDao;
import com.example.gestionacademica.database.entities.Inscripcion;
import com.example.gestionacademica.database.relations.InscripcionCompleta;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InscripcionRepository {
    private InscripcionDao inscripcionDao;
    private ExecutorService executorService;

    public InscripcionRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        inscripcionDao = database.inscripcionDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(Inscripcion inscripcion, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                long id = inscripcionDao.insert(inscripcion);
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

    public void update(Inscripcion inscripcion, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                inscripcionDao.update(inscripcion);
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

    public void delete(Inscripcion inscripcion, OnOperationCompleteListener listener) {
        executorService.execute(() -> {
            try {
                inscripcionDao.delete(inscripcion);
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

    public void getInscripcionesCompletas(OnDataLoadedListener<List<InscripcionCompleta>> listener) {
        executorService.execute(() -> {
            try {
                List<InscripcionCompleta> inscripciones = inscripcionDao.getInscripcionesCompletas();
                if (listener != null) {
                    listener.onDataLoaded(inscripciones);
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