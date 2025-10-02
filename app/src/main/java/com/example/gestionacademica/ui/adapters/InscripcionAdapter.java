package com.example.gestionacademica.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionacademica.database.relations.InscripcionCompleta;
import com.example.gestionacademica.databinding.ItemInscripcionBinding;

import java.util.ArrayList;
import java.util.List;

public class InscripcionAdapter extends RecyclerView.Adapter<InscripcionAdapter.InscripcionViewHolder> {

    private List<InscripcionCompleta> inscripciones = new ArrayList<>();
    private OnInscripcionClickListener listener;

    public interface OnInscripcionClickListener {
        void onDeleteClick(InscripcionCompleta inscripcion);
    }

    public InscripcionAdapter(OnInscripcionClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public InscripcionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInscripcionBinding binding = ItemInscripcionBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new InscripcionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InscripcionViewHolder holder, int position) {
        holder.bind(inscripciones.get(position));
    }

    @Override
    public int getItemCount() {
        return inscripciones.size();
    }

    public void setInscripciones(List<InscripcionCompleta> inscripciones) {
        this.inscripciones = inscripciones;
        notifyDataSetChanged();
    }

    class InscripcionViewHolder extends RecyclerView.ViewHolder {
        private final ItemInscripcionBinding binding;

        public InscripcionViewHolder(ItemInscripcionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(InscripcionCompleta inscripcion) {
            binding.tvEstudianteInscripcion.setText("Estudiante: " +
                    inscripcion.estudiante.getNombreCompleto());
            binding.tvMateriaInscripcion.setText("Materia: " +
                    inscripcion.materia.getNombre());
            binding.tvPeriodoInscripcion.setText("Periodo: " +
                    inscripcion.inscripcion.getAnio() + " - " +
                    inscripcion.inscripcion.getSemestre());
            binding.tvFechaInscripcion.setText("Fecha: " +
                    inscripcion.inscripcion.getFechaInscripcion());

            binding.btnEliminarInscripcion.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(inscripcion);
                }
            });
        }
    }
}