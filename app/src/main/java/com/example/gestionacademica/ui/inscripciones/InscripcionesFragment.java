package com.example.gestionacademica.ui.inscripciones;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gestionacademica.database.entities.Estudiante;
import com.example.gestionacademica.database.entities.Inscripcion;
import com.example.gestionacademica.database.entities.Materia;
import com.example.gestionacademica.database.relations.InscripcionCompleta;
import com.example.gestionacademica.databinding.DialogInscripcionBinding;
import com.example.gestionacademica.databinding.FragmentInscripcionesBinding;
import com.example.gestionacademica.repository.EstudianteRepository;
import com.example.gestionacademica.repository.InscripcionRepository;
import com.example.gestionacademica.repository.MateriaRepository;
import com.example.gestionacademica.ui.adapters.InscripcionAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InscripcionesFragment extends Fragment {

    private FragmentInscripcionesBinding binding;
    private InscripcionAdapter adapter;
    private InscripcionRepository inscripcionRepository;
    private EstudianteRepository estudianteRepository;
    private MateriaRepository materiaRepository;
    private Handler mainHandler;

    private List<Estudiante> listaEstudiantes = new ArrayList<>();
    private List<Materia> listaMaterias = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInscripcionesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainHandler = new Handler(Looper.getMainLooper());
        inscripcionRepository = new InscripcionRepository(requireActivity().getApplication());
        estudianteRepository = new EstudianteRepository(requireActivity().getApplication());
        materiaRepository = new MateriaRepository(requireActivity().getApplication());

        setupRecyclerView();
        setupFab();
        loadInscripciones();
    }

    private void setupRecyclerView() {
        adapter = new InscripcionAdapter(inscripcion -> showDeleteConfirmDialog(inscripcion));
        binding.recyclerViewInscripciones.setAdapter(adapter);
    }

    private void setupFab() {
        binding.fabAgregarInscripcion.setOnClickListener(v -> {
            loadEstudiantesYMaterias();
        });
    }

    private void loadInscripciones() {
        inscripcionRepository.getInscripcionesCompletas(
                new InscripcionRepository.OnDataLoadedListener<List<InscripcionCompleta>>() {
                    @Override
                    public void onDataLoaded(List<InscripcionCompleta> data) {
                        mainHandler.post(() -> {
                            adapter.setInscripciones(data);
                            binding.tvEmptyInscripciones.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                        });
                    }

                    @Override
                    public void onError(String error) {
                        mainHandler.post(() ->
                                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void loadEstudiantesYMaterias() {
        estudianteRepository.getAllEstudiantes(
                new EstudianteRepository.OnDataLoadedListener<List<Estudiante>>() {
                    @Override
                    public void onDataLoaded(List<Estudiante> data) {
                        listaEstudiantes = data;
                        materiaRepository.getAllMaterias(
                                new MateriaRepository.OnDataLoadedListener<List<Materia>>() {
                                    @Override
                                    public void onDataLoaded(List<Materia> data) {
                                        listaMaterias = data;
                                        mainHandler.post(() -> showInscripcionDialog());
                                    }

                                    @Override
                                    public void onError(String error) {
                                        mainHandler.post(() ->
                                                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
                                    }
                                });
                    }

                    @Override
                    public void onError(String error) {
                        mainHandler.post(() ->
                                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void showInscripcionDialog() {
        if (listaEstudiantes.isEmpty()) {
            Toast.makeText(getContext(), "Primero debe agregar estudiantes", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listaMaterias.isEmpty()) {
            Toast.makeText(getContext(), "Primero debe agregar materias", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogInscripcionBinding dialogBinding = DialogInscripcionBinding.inflate(getLayoutInflater());

        // Configurar spinner de estudiantes
        List<String> nombresEstudiantes = new ArrayList<>();
        for (Estudiante e : listaEstudiantes) {
            nombresEstudiantes.add(e.getNombreCompleto());
        }
        ArrayAdapter<String> estudiantesAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                nombresEstudiantes
        );
        dialogBinding.spinnerEstudiante.setAdapter(estudiantesAdapter);

        // Configurar spinner de materias
        List<String> nombresMaterias = new ArrayList<>();
        for (Materia m : listaMaterias) {
            nombresMaterias.add(m.getNombre());
        }
        ArrayAdapter<String> materiasAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                nombresMaterias
        );
        dialogBinding.spinnerMateria.setAdapter(materiasAdapter);

        // Configurar fecha actual
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dialogBinding.etFecha.setText(dateFormat.format(calendar.getTime()));

        // Configurar año actual
        dialogBinding.etAnio.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        dialogBinding.etFecha.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        dialogBinding.etFecha.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        AlertDialog dialog = builder.setView(dialogBinding.getRoot()).create();

        dialogBinding.btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.btnGuardar.setOnClickListener(v -> {
            String estudianteSeleccionado = dialogBinding.spinnerEstudiante.getText().toString();
            String materiaSeleccionada = dialogBinding.spinnerMateria.getText().toString();
            String semestre = dialogBinding.etSemestre.getText().toString().trim();
            String anio = dialogBinding.etAnio.getText().toString().trim();
            String fecha = dialogBinding.etFecha.getText().toString().trim();

            if (estudianteSeleccionado.isEmpty() || materiaSeleccionada.isEmpty() ||
                    semestre.isEmpty() || anio.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener IDs
            int estudianteId = 0;
            for (Estudiante e : listaEstudiantes) {
                if (e.getNombreCompleto().equals(estudianteSeleccionado)) {
                    estudianteId = e.getId();
                    break;
                }
            }

            int materiaId = 0;
            for (Materia m : listaMaterias) {
                if (m.getNombre().equals(materiaSeleccionada)) {
                    materiaId = m.getId();
                    break;
                }
            }

            Inscripcion nuevaInscripcion = new Inscripcion(estudianteId, materiaId, semestre, anio, fecha);

            inscripcionRepository.insert(nuevaInscripcion,
                    new InscripcionRepository.OnOperationCompleteListener() {
                        @Override
                        public void onSuccess(long id) {
                            mainHandler.post(() -> {
                                Toast.makeText(getContext(), "Inscripción agregada", Toast.LENGTH_SHORT).show();
                                loadInscripciones();
                                dialog.dismiss();
                            });
                        }

                        @Override
                        public void onError(String error) {
                            mainHandler.post(() ->
                                    Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
                        }
                    });
        });

        dialog.show();
    }

    private void showDeleteConfirmDialog(InscripcionCompleta inscripcion) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar eliminación")
                .setMessage("¿Está seguro de eliminar esta inscripción?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    inscripcionRepository.delete(inscripcion.inscripcion,
                            new InscripcionRepository.OnOperationCompleteListener() {
                                @Override
                                public void onSuccess(long id) {
                                    mainHandler.post(() -> {
                                        Toast.makeText(getContext(), "Inscripción eliminada", Toast.LENGTH_SHORT).show();
                                        loadInscripciones();
                                    });
                                }

                                @Override
                                public void onError(String error) {
                                    mainHandler.post(() ->
                                            Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
                                }
                            });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}