package com.localcash.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.localcash.R;
import com.localcash.database.DatabaseManager;

import java.util.ArrayList;

public class GastosCompartidosActivity extends AppCompatActivity {

    private EditText etDescripcion, etMonto;
    private Spinner spContactos;
    private Button btnDividir;
    private DatabaseManager dbManager;
    private ArrayList<Integer> listaIdsContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_compartidos);

        dbManager = new DatabaseManager(this);

        etDescripcion = findViewById(R.id.etDescripcionGasto);
        etMonto = findViewById(R.id.etMontoGasto);
        spContactos = findViewById(R.id.spContactos);
        btnDividir = findViewById(R.id.btnDividirGasto);

        listaIdsContactos = new ArrayList<>();
        cargarSpinnerContactos();

        btnDividir.setOnClickListener(v -> procesarDivision());
    }

    private void cargarSpinnerContactos() {
        ArrayList<String> nombres = new ArrayList<>();
        Cursor cursor = dbManager.obtenerContactos();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                listaIdsContactos.add(id);
                nombres.add(nombre);
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombres);
        spContactos.setAdapter(adapter);
    }

    private void procesarDivision() {
        String desc = etDescripcion.getText().toString().trim();
        String montoStr = etMonto.getText().toString().trim();

        if (desc.isEmpty() || montoStr.isEmpty() || listaIdsContactos.isEmpty()) {
            Toast.makeText(this, "Complete los campos y asegúrese de tener contactos guardados", Toast.LENGTH_SHORT).show();
            return;
        }

        double montoTotal = Double.parseDouble(montoStr);
        int posicionSeleccionada = spContactos.getSelectedItemPosition();
        int contactoId = listaIdsContactos.get(posicionSeleccionada);

        boolean ok = dbManager.registrarGastoCompartido(desc, montoTotal, contactoId);
        if (ok) {
            double mitad = montoTotal / 2.0;
            Toast.makeText(this, "Deuda registrada: Contacto debe $" + mitad, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Error al procesar división", Toast.LENGTH_SHORT).show();
        }
    }
}