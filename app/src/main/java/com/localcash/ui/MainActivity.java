package com.localcash.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.localcash.R;
import com.localcash.security.BiometricAuthManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Autenticación Biométrica
        BiometricAuthManager.authenticate(this, new BiometricAuthManager.AuthCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Acceso Autorizado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "Error de acceso: " + error, Toast.LENGTH_LONG).show();
            }
        });

        // Eventos Click en Tarjetas
        findViewById(R.id.cardNuevoMovimiento).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, NuevoMovimientoActivity.class)));

        findViewById(R.id.cardVerMapa).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, MapActivity.class)));

        findViewById(R.id.cardContactos).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ContactosActivity.class)));

        findViewById(R.id.cardGastos).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GastosCompartidosActivity.class)));

        findViewById(R.id.cardResumenDeudas).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ResumenDeudasActivity.class)));}
}