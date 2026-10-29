package com.localcash.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.localcash.R;
import com.localcash.database.DatabaseManager;

import java.util.ArrayList;

public class ContactosActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono;
    private Button btnGuardar;
    private ListView lvContactos;
    private DatabaseManager dbManager;
    private ArrayList<String> listaContactos;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        dbManager = new DatabaseManager(this);

        etNombre = findViewById(R.id.etNombreContacto);
        etTelefono = findViewById(R.id.etTelefonoContacto);
        btnGuardar = findViewById(R.id.btnGuardarContacto);
        lvContactos = findViewById(R.id.lvContactos);

        listaContactos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaContactos);
        lvContactos.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> guardarContacto());
        cargarContactos();
    }

    private void guardarContacto() {
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingrese el nombre del contacto", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbManager.agregarContacto(nombre, telefono, "");
        if (id != -1) {
            Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show();
            etNombre.setText("");
            etTelefono.setText("");
            cargarContactos();
        }
    }

    private void cargarContactos() {
        listaContactos.clear();
        Cursor cursor = dbManager.obtenerContactos();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
                listaContactos.add(nombre + " (" + telefono + ")");
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }
}