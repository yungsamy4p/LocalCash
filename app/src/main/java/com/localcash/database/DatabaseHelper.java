package com.localcash.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "localcash.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contactos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, telefono TEXT, email TEXT);");
        db.execSQL("CREATE TABLE cuentas (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre_cuenta TEXT NOT NULL, saldo REAL NOT NULL DEFAULT 0.0, tipo TEXT NOT NULL);");
        db.execSQL("CREATE TABLE movimientos (id INTEGER PRIMARY KEY AUTOINCREMENT, cuenta_id INTEGER NOT NULL, monto REAL NOT NULL, tipo TEXT NOT NULL, concepto TEXT, fecha TEXT NOT NULL, latitud REAL, longitud REAL, FOREIGN KEY(cuenta_id) REFERENCES cuentas(id));");
        db.execSQL("CREATE TABLE compras (id INTEGER PRIMARY KEY AUTOINCREMENT, contacto_id INTEGER, descripcion TEXT NOT NULL, monto_estimado REAL NOT NULL, fecha_programada TEXT, estado TEXT DEFAULT 'PENDIENTE');");
        db.execSQL("CREATE TABLE gastos_compartidos (id INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT NOT NULL, monto_total REAL NOT NULL, fecha TEXT NOT NULL);");
        db.execSQL("CREATE TABLE detalle_deudas (id INTEGER PRIMARY KEY AUTOINCREMENT, gasto_id INTEGER NOT NULL, contacto_id INTEGER NOT NULL, monto_asignado REAL NOT NULL, estado_pago INTEGER DEFAULT 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS detalle_deudas");
        db.execSQL("DROP TABLE IF EXISTS gastos_compartidos");
        db.execSQL("DROP TABLE IF EXISTS compras");
        db.execSQL("DROP TABLE IF EXISTS movimientos");
        db.execSQL("DROP TABLE IF EXISTS cuentas");
        db.execSQL("DROP TABLE IF EXISTS contactos");
        onCreate(db);
    }
}