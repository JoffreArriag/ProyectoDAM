package com.example.login.tarea;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.RegistroActivity;
import com.example.login.agricultor.Agricultor;
import com.example.login.agricultor.AgricultoresAdapter;
import com.example.login.cultivo.Cultivo;
import com.example.login.cultivo.CultivoAdapter;
import com.example.login.inventario.InsumoAdapter;
import com.example.login.inventario.InsumoAgricola;
import com.example.login.inventario.InventarioActivity;
import com.example.login.mercado.MercadoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AgregarTareaDialog extends DialogFragment {

    Spinner spinnerCultivo ;
    EditText editNombre ;
    EditText editDescripcion ;
    EditText editFechaInicio ;
    EditText editFechaFin ;
    String fechaNacimiento = "";
    AgricultoresAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    InsumoAdapter adapterInsumo;
    TareaAdapter tareaAdapter;
    CultivoAdapter adapterCultivo = new CultivoAdapter(new ArrayList<>());
    private List<Cultivo> listaCultivosGlobal;


    private DatabaseReference ref;
    public interface TareaListener {
        void onTareaAgregado(Tarea tarea);
        void onTareaEditado(Tarea tarea, int position);
    }

    private TareaListener listener;
    private Tarea tareaExistente;
    private int editarPos = -1;

    public void setTareaListener(TareaListener listener) {
        this.listener = listener;
    }

    public void setTareaEditar(Tarea insumo, int position) {
        this.tareaExistente = insumo;
        this.editarPos = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_tarea, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()).setView(view);

        recyclerView = view.findViewById(R.id.recyclerAgricultores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView2 = view.findViewById(R.id.recyclerInsumos);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));

        spinnerCultivo = view.findViewById(R.id.spinnerCultivo);
         editNombre = view.findViewById(R.id.editNombre);
         editDescripcion = view.findViewById(R.id.editDescripcion);
         editFechaInicio = view.findViewById(R.id.txtDateInicio);
         editFechaFin = view.findViewById(R.id.txtDateFin);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        Button btnCancelar = view.findViewById(R.id.btnVolver);


        cargarAgricultoresDesdeFirebase();
        cargarInsumosDesdeFirebase();
        cargarCultivosDesdeFirebase();


        if (tareaExistente != null) {
            spinnerCultivo.setSelection(adapterCultivo.getPosition(tareaExistente.getCultivo()));
            editNombre.setText(tareaExistente.getNombre());
            editDescripcion.setText(tareaExistente.getDescripcion());
            editFechaInicio.setText(String.valueOf(tareaExistente.getFecha_inico()));
            editFechaFin.setText(String.valueOf(tareaExistente.getFecha_fin()));
        }
        btnGuardar.setOnClickListener(this::guardarTarea);
        btnCancelar.setOnClickListener(this::cancelarTarea);
        editFechaInicio.setOnClickListener(this::seleccionarFechaInicio);
        editFechaFin.setOnClickListener(this::seleccionarFechaFin);


        return builder.create();
    }



    private void cargarAgricultoresDesdeFirebase() {
        ref = FirebaseDatabase.getInstance().getReference("agricultores");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Agricultor> listaAgricultures = new ArrayList<>();
                for (DataSnapshot agricultorSnapshot : snapshot.getChildren()) {
                    Agricultor agricultor = agricultorSnapshot.getValue(Agricultor.class);
                    if (agricultor != null) {
                        listaAgricultures.add(agricultor);
                    }
                }
                adapter = new AgricultoresAdapter(listaAgricultures);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar agricultores", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void cargarCultivosDesdeFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cultivos");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Cultivo> listaCultivos = new ArrayList<>();
                for (DataSnapshot cultivoSnapshot : snapshot.getChildren()) {
                    Cultivo cultivo = cultivoSnapshot.getValue(Cultivo.class);
                    if (cultivo != null) {
                        listaCultivos.add(cultivo);
                    }
                }

                // Guardamos la lista en el adapter si lo necesitas para otra cosa
                adapterCultivo = new CultivoAdapter(listaCultivos);

                // Extraemos los nombres para el Spinner
                List<String> nombresCultivos = new ArrayList<>();
                for (Cultivo cultivo : listaCultivos) {
                    nombresCultivos.add(cultivo.getNombre());
                }


                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, nombresCultivos);
                spinnerCultivo.setAdapter(spinnerAdapter);


                listaCultivosGlobal = listaCultivos;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar cultivos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarInsumosDesdeFirebase() {
        ref = FirebaseDatabase.getInstance().getReference("insumos");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<InsumoAgricola> listaInsumo = new ArrayList<>();
                for (DataSnapshot insumoSnapshot : snapshot.getChildren()) {
                    InsumoAgricola insumo = insumoSnapshot.getValue(InsumoAgricola.class);
                    if (insumo != null) {
                        listaInsumo.add(insumo);
                    }
                }
                adapterInsumo = new InsumoAdapter(listaInsumo);
                recyclerView2.setAdapter(adapterInsumo);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void seleccionarFechaInicio(View v) {
        Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                getActivity(),
                (view1, year, month, dayOfMonth) -> {
                    fechaNacimiento = dayOfMonth + "/" + (month + 1) + "/" + year;
                    editFechaInicio.setText(fechaNacimiento);
                },
                año, mes, dia
        );
        datePicker.show();
    }
    public void seleccionarFechaFin(View v) {
        Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                getActivity(),
                (view1, year, month, dayOfMonth) -> {
                    fechaNacimiento = dayOfMonth + "/" + (month + 1) + "/" + year;
                    editFechaFin.setText(fechaNacimiento);
                },
                año, mes, dia
        );
        datePicker.show();
    }
    public void guardarTarea(View v) {
        String id = tareaExistente != null ? tareaExistente.getId() : UUID.randomUUID().toString();
        // Obtener el cultivo seleccionado del Spinner
        String cultivoSeleccionado = spinnerCultivo.getSelectedItem().toString();
        String nombre = editNombre.getText().toString().trim();
        String descripcion = editDescripcion.getText().toString().trim();
        String fecha_inico = editFechaInicio.getText().toString().trim();
        String fecha_fin = editFechaFin.getText().toString().trim();

        List<Agricultor> agricultores = adapter.getAgricultoresSeleccionados();
        if (agricultores.isEmpty()) {
            Toast.makeText(getContext(), "No hay agricultores seleccionados", Toast.LENGTH_SHORT).show();
            return;
        }
        List<InsumoAgricola> insumos = adapterInsumo.getInsumoSeleccionados();
        if (insumos.isEmpty()) {
            Toast.makeText(getContext(), "No hay insumos seleccionados", Toast.LENGTH_SHORT).show();
            return;
        }
        if (descripcion.isEmpty() || nombre.isEmpty() || fecha_fin.isEmpty() || fecha_inico.isEmpty()) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder agricultoresCadena = new StringBuilder();
        for (Agricultor c : agricultores) {
            agricultoresCadena.append(c.getNombre()).append("; ");
        }

        StringBuilder insumosCadena = new StringBuilder();
        for (InsumoAgricola c : insumos) {
            insumosCadena.append(c.getNombre()).append("; ");
        }

        try {
            ref = FirebaseDatabase.getInstance().getReference("Tareas");
            Tarea tarea = new Tarea(id, nombre, descripcion, cultivoSeleccionado, fecha_inico, fecha_fin,
                    agricultoresCadena.toString(), insumosCadena.toString());

            if (editarPos >= 0 && listener != null) {
                // Editar en Firebase
                ref.child(id).setValue(tarea)
                        .addOnSuccessListener(task -> {
                            listener.onTareaEditado(tarea, editarPos);
                            dismiss();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Error al editar tarea", Toast.LENGTH_SHORT).show()
                        );
            } else {
                // Agregar nuevo a Firebase
                ref.child(id).setValue(tarea)
                        .addOnSuccessListener(task -> {
                            listener.onTareaAgregado(tarea);
                            dismiss();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Error al guardar tarea", Toast.LENGTH_SHORT).show()
                        );
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Cantidad debe ser numérica", Toast.LENGTH_SHORT).show();
        }
    }
    public void cancelarTarea(View v) {
        dismiss();
    }



}
