package com.localcash.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import com.localcash.R;
import com.localcash.database.DatabaseManager;

public class MapActivity extends AppCompatActivity {

    private MapView map;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));

        setContentView(R.layout.activity_map);

        map = findViewById(R.id.mapView);
        map.setMultiTouchControls(true);

        dbManager = new DatabaseManager(this);

        // Coordenadas iniciales (Santiago, Chile)
        GeoPoint startPoint = new GeoPoint(-33.4489, -70.6693);
        map.getController().setZoom(14.0);
        map.getController().setCenter(startPoint);
    }

    private void cargarMarcadoresDesdeBD() {
        // Limpiar marcas anteriores para evitar duplicados al recargar
        map.getOverlays().clear();

        Cursor cursor = dbManager.obtenerMovimientosConUbicacion();
        int contador = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                double lat = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud"));
                double lng = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"));
                String concepto = cursor.getString(cursor.getColumnIndexOrThrow("concepto"));
                double monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto"));

                // Validar que las coordenadas sean válidas (diferentes de 0)
                if (lat != 0.0 && lng != 0.0) {
                    GeoPoint punto = new GeoPoint(lat, lng);
                    Marker marker = new Marker(map);
                    marker.setPosition(punto);
                    marker.setTitle(concepto + " - $" + monto);
                    map.getOverlays().add(marker);

                    // Centrar el mapa en la última coordenada registrada
                    map.getController().setCenter(punto);
                    contador++;
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (contador == 0) {
            Toast.makeText(this, "No hay movimientos con ubicación guardada", Toast.LENGTH_SHORT).show();
        }

        map.invalidate(); // Forzar el redibujado del mapa
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
            cargarMarcadoresDesdeBD(); // Recargar los puntos de la BD al abrir/volver a la pantalla
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (map != null) map.onPause();
    }
}