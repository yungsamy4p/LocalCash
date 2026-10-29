package com.localcash.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.localcash.R;
import com.localcash.database.DatabaseManager;

import java.util.ArrayList;

public class ResumenDeudasActivity extends AppCompatActivity {

    private TextView tvTotalAcumulado;
    private ListView lvDetalleDeudas;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_deudas);

        dbManager = new DatabaseManager(this);

        tvTotalAcumulado = findViewById(R.id.tvTotalAcumulado);
        lvDetalleDeudas = findViewById(R.id.lvDetalleDeudas);

        cargarResumenDeudas();
    }

    private void cargarResumenDeudas() {
        // Mostrar total acumulado
        double total = dbManager.obtenerTotalDeudasAcumuladas();
        tvTotalAcumulado.setText("$" + String.format("%.2f", total));

        ArrayList<String> listaDetalle = new ArrayList<>();
        Cursor cursor = dbManager.obtenerDetalleDeudasPorContacto();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                double deuda = cursor.getDouble(cursor.getColumnIndexOrThrow("total_deuda"));
                listaDetalle.add(nombre + " -> Debe $" + String.format("%.2f", deuda));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            listaDetalle.add("No hay deudas pendientes registradas.");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaDetalle);
        lvDetalleDeudas.setAdapter(adapter);
    }
}