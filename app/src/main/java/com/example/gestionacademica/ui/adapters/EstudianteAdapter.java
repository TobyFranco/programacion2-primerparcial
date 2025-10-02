package com.example.gestionacademica.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionacademica.database.entities.Estudiante;
import com.example.gestionacademica.databinding.ItemEstudianteBinding;

import java.util.ArrayList;
import java.util.List;

public class EstudianteAdapter extends RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder> {

    private List<Estudiante> estudiantes = new ArrayList<>();
    private OnEstudianteClickListener listener;

    public interface OnEstudianteClickListener {
        void onEditClick(Estudiante estudiante);
        void onDeleteClick(Estudiante estudiante);
    }

    public EstudianteAdapter(OnEstudianteClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EstudianteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEstudianteBinding binding = ItemEstudianteBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new EstudianteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EstudianteViewHolder holder, int position) {
        holder.bind(estudiantes.get(position));
    }

    @Override
    public int getItemCount() {
        return estudiantes.size();
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
        notifyDataSetChanged();
    }

    class EstudianteViewHolder extends RecyclerView.ViewHolder {
        private final ItemEstudianteBinding binding;

        public EstudianteViewHolder(ItemEstudianteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Estudiante estudiante) {
            binding.tvNombreEstudiante.setText(estudiante.getNombreCompleto());
            binding.tvCarnetEstudiante.setText("Carnet: " + estudiante.getCarnet());
            binding.tvEmailEstudiante.setText(estudiante.getEmail());

            binding.btnEditarEstudiante.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(estudiante);
                }
            });

            binding.btnEliminarEstudiante.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(estudiante);
                }
            });
        }
    }
}