package com.example.gestionacademica.ui.materias;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gestionacademica.database.entities.Materia;
import com.example.gestionacademica.databinding.DialogMateriaBinding;
import com.example.gestionacademica.databinding.FragmentMateriasBinding;
import com.example.gestionacademica.repository.MateriaRepository;
import com.example.gestionacademica.ui.adapters.MateriaAdapter;

import java.util.List;

public class MateriasFragment extends Fragment {

    private FragmentMateriasBinding binding;
    private MateriaAdapter adapter;
    private MateriaRepository repository;
    private Handler mainHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMateriasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainHandler = new Handler(Looper.getMainLooper());
        repository = new MateriaRepository(requireActivity().getApplication());

        setupRecyclerView();
        setupFab();
        loadMaterias();
    }

    private void setupRecyclerView() {
        adapter = new MateriaAdapter(new MateriaAdapter.OnMateriaClickListener() {
            @Override
            public void onEditClick(Materia materia) {
                showMateriaDialog(materia);
            }

            @Override
            public void onDeleteClick(Materia materia) {
                showDeleteConfirmDialog(materia);
            }
        });
        binding.recyclerViewMaterias.setAdapter(adapter);
    }

    private void setupFab() {
        binding.fabAgregarMateria.setOnClickListener(v -> showMateriaDialog(null));
    }

    private void loadMaterias() {
        repository.getAllMaterias(new MateriaRepository.OnDataLoadedListener<List<Materia>>() {
            @Override
            public void onDataLoaded(List<Materia> data) {
                mainHandler.post(() -> {
                    adapter.setMaterias(data);
                    binding.tvEmptyMaterias.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
                });
            }

            @Override
            public void onError(String error) {
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showMateriaDialog(Materia materia) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogMateriaBinding dialogBinding = DialogMateriaBinding.inflate(getLayoutInflater());

        boolean isEdit = materia != null;

        if (isEdit) {
            dialogBinding.tvTituloDialog.setText("Editar Materia");
            dialogBinding.etCodigo.setText(materia.getCodigo());
            dialogBinding.etNombre.setText(materia.getNombre());
            dialogBinding.etCreditos.setText(String.valueOf(materia.getCreditos()));
            dialogBinding.etDescripcion.setText(materia.getDescripcion());
        } else {
            dialogBinding.tvTituloDialog.setText("Agregar Materia");
        }

        AlertDialog dialog = builder.setView(dialogBinding.getRoot()).create();

        dialogBinding.btnCancelar.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.btnGuardar.setOnClickListener(v -> {
            String codigo = dialogBinding.etCodigo.getText().toString().trim();
            String nombre = dialogBinding.etNombre.getText().toString().trim();
            String creditosStr = dialogBinding.etCreditos.getText().toString().trim();
            String descripcion = dialogBinding.etDescripcion.getText().toString().trim();

            if (codigo.isEmpty() || nombre.isEmpty() || creditosStr.isEmpty()) {
                Toast.makeText(getContext(), "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            int creditos;
            try {
                creditos = Integer.parseInt(creditosStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Créditos inválidos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEdit) {
                materia.setCodigo(codigo);
                materia.setNombre(nombre);
                materia.setCreditos(creditos);
                materia.setDescripcion(descripcion);

                repository.update(materia, new MateriaRepository.OnOperationCompleteListener() {
                    @Override
                    public void onSuccess(long id) {
                        mainHandler.post(() -> {
                            Toast.makeText(getContext(), "Materia actualizada", Toast.LENGTH_SHORT).show();
                            loadMaterias();
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
                Materia nuevaMateria = new Materia(codigo, nombre, creditos, descripcion);

                repository.insert(nuevaMateria, new MateriaRepository.OnOperationCompleteListener() {
                    @Override
                    public void onSuccess(long id) {
                        mainHandler.post(() -> {
                            Toast.makeText(getContext(), "Materia agregada", Toast.LENGTH_SHORT).show();
                            loadMaterias();
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

    private void showDeleteConfirmDialog(Materia materia) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar eliminación")
                .setMessage("¿Está seguro de eliminar la materia " + materia.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    repository.delete(materia, new MateriaRepository.OnOperationCompleteListener() {
                        @Override
                        public void onSuccess(long id) {
                            mainHandler.post(() -> {
                                Toast.makeText(getContext(), "Materia eliminada", Toast.LENGTH_SHORT).show();
                                loadMaterias();
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