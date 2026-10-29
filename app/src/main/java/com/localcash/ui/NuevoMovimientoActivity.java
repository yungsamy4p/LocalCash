package com.localcash.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.localcash.R;
import com.localcash.database.DatabaseManager;

public class NuevoMovimientoActivity extends AppCompatActivity {

    private EditText etConcepto, etMonto, etLatitud, etLongitud;
    private Button btnGuardar;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_movimiento);

        dbManager = new DatabaseManager(this);

        etConcepto = findViewById(R.id.etConcepto);
        etMonto = findViewById(R.id.etMonto);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v -> guardarMovimiento());
    }

    private void guardarMovimiento() {
        String concepto = etConcepto.getText().toString().trim();
        String montoStr = etMonto.getText().toString().trim();
        String latStr = etLatitud.getText().toString().trim();
        String lngStr = etLongitud.getText().toString().trim();

        if (concepto.isEmpty() || montoStr.isEmpty()) {
            Toast.makeText(this, "Complete el concepto y el monto", Toast.LENGTH_SHORT).show();
            return;
        }

        double monto = Double.parseDouble(montoStr);
        double lat = latStr.isEmpty() ? -33.4489 : Double.parseDouble(latStr);
        double lng = lngStr.isEmpty() ? -70.6693 : Double.parseDouble(lngStr);

        long resultado = dbManager.registrarMovimiento(1, monto, "EGRESO", concepto, "2026-10-29", lat, lng);

        if (resultado != -1) {
            Toast.makeText(this, "Movimiento guardado localmente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}