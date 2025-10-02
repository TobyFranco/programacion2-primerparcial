package com.example.gestionacademica.ui.estudiantes;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gestionacademica.database.entities.Estudiante;
import com.example.gestionacademica.databinding.DialogEstudianteBinding;
import com.example.gestionacademica.databinding.FragmentEstudiantesBinding;
import com.example.gestionacademica.repository.EstudianteRepository;
import com.example.gestionacademica.ui.adapters.EstudianteAdapter;

import java.util.List;

public class EstudiantesFragment extends Fragment {

    private FragmentEstudiantesBinding binding;
    private EstudianteAdapter adapter;
    private EstudianteRepository repository;
    private Handler mainHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEstudiantesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainHandler = new Handler(Looper.getMainLooper());
        repository = new EstudianteRepository(requireActivity().getApplication());

        setupRecyclerView();
        setupSearchView();
        setupFab();
        loadEstudiantes();
    }

    private void setupRecyclerView() {
        adapter = new EstudianteAdapter(new EstudianteAdapter.OnEstudianteClickListener() {
            @Override
            public void onEditClick(Estudiante estudiante) {
                showEstudianteDialog(estudiante);
            }

            @Override
            public void onDeleteClick(Estudiante estudiante) {
                showDeleteConfirmDialog(estudiante);
            }
        });
        binding.recyclerViewEstudiantes.setAdapter(adapter);
    }

    private void setupSearchView() {
        binding.etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    buscarEstudiantes(s.toString());
                } else {
                    loadEstudiantes();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFab() {
        binding.fabAgregarEstudiante.setOnClickListener(v -> showEstudianteDialog(null));
    }

    private void loadEstudiantes() {
        repository.getAllEstudiantes(new EstudianteRepository.OnDataLoadedListener<List<Estudiante>>() {
            @Override
            public void onDataLoaded(List<Estudiante> data) {
                mainHandler.post(() -> {
                    adapter.setEstudiantes(data);
                    binding.tvEmptyEstudiantes.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                });
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void buscarEstudiantes(String busqueda) {
        repository.buscarEstudiantes(busqueda, new EstudianteRepository.OnDataLoadedListener<List<Estudiante>>() {
            @Override
            public void onDataLoaded(List<Estudiante> data) {
                mainHandler.post(() -> {
                    adapter.setEstudiantes(data);
                    binding.tvEmptyEstudiantes.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                });
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showEstudianteDialog(Estudiante estudiante) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogEstudianteBinding dialogBinding = DialogEstudianteBinding.inflate(getLayoutInflater());

        boolean isEdit = estudiante != null;

        if (isEdit) {
            dialogBinding.tvTituloDialog.setText("Editar Estudiante");
            dialogBinding.etNombre.setText(estudiante.getNombre());
            dialogBinding.etApellido.setText(estudiante.getApellido());
            dialogBinding.etCarnet.setText(estudiante.getCarnet());
            dialogBinding.etEmail.setText(estudiante.getEmail());
            dialogBinding.etTelefono.setText(estudiante.getTelefono());
        } else {
            dialogBinding.tvTituloDialog.setText("Agregar Estudiante");
        }

        AlertDialog dialog = builder.setView(dialogBinding.getRoot()).create();

        dialogBinding.btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.btnGuardar.setOnClickListener(v -> {
            String nombre = dialogBinding.etNombre.getText().toString().trim();
            String apellido = dialogBinding.etApellido.getText().toString().trim();
            String carnet = dialogBinding.etCarnet.getText().toString().trim();
            String email = dialogBinding.etEmail.getText().toString().trim();
            String telefono = dialogBinding.etTelefono.getText().toString().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || carnet.isEmpty()) {
                Toast.makeText(getContext(), "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEdit) {
                estudiante.setNombre(nombre);
                estudiante.setApellido(apellido);
                estudiante.setCarnet(carnet);
                estudiante.setEmail(email);
                estudiante.setTelefono(telefono);

                repository.update(estudiante, new EstudianteRepository.OnOperationCompleteListener() {
                    @Override
                    public void onSuccess(long id) {
                        mainHandler.post(() -> {
                            Toast.makeText(getContext(), "Estudiante actualizado", Toast.LENGTH_SHORT).show();
                            loadEstudiantes();
                            dialog.dismiss();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        mainHandler.post(() ->
                                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Estudiante nuevoEstudiante = new Estudiante(nombre, apellido, carnet, email, telefono);

                repository.insert(nuevoEstudiante, new EstudianteRepository.OnOperationCompleteListener() {
                    @Override
                    public void onSuccess(long id) {
                        mainHandler.post(() -> {
                            Toast.makeText(getContext(), "Estudiante agregado", Toast.LENGTH_SHORT).show();
                            loadEstudiantes();
                            dialog.dismiss();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        mainHandler.post(() ->
                                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });

        dialog.show();
    }

    private void showDeleteConfirmDialog(Estudiante estudiante) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar eliminación")
                .setMessage("¿Está seguro de eliminar a " + estudiante.getNombreCompleto() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    repository.delete(estudiante, new EstudianteRepository.OnOperationCompleteListener() {
                        @Override
                        public void onSuccess(long id) {
                            mainHandler.post(() -> {
                                Toast.makeText(getContext(), "Estudiante eliminado", Toast.LENGTH_SHORT).show();
                                loadEstudiantes();
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