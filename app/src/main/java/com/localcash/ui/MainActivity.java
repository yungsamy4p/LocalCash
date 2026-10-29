package com.localcash.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.localcash.R;
import com.localcash.security.BiometricAuthManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        findViewById(R.id.btnNuevoMovimiento).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, NuevoMovimientoActivity.class)));

        findViewById(R.id.btnVerMapa).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, MapActivity.class)));

        findViewById(R.id.btnContactos).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ContactosActivity.class)));

        findViewById(R.id.btnGastosCompartidos).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, GastosCompartidosActivity.class)));
    }
}