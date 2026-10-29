package com.localcash.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

    private DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Insertar Contacto
    public long agregarContacto(String nombre, String telefono, String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("telefono", telefono);
        values.put("email", email);
        return db.insert("contactos", null, values);
    }

    // Insertar Movimiento con Coordenadas
    public long registrarMovimiento(int cuentaId, double monto, String tipo, String concepto, String fecha, double lat, double lng) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cuenta_id", cuentaId);
        values.put("monto", monto);
        values.put("tipo", tipo);
        values.put("concepto", concepto);
        values.put("fecha", fecha);
        values.put("latitud", lat);
        values.put("longitud", lng);
        return db.insert("movimientos", null, values);
    }

    // Obtener Movimientos con Ubicación para el Mapa
    public Cursor obtenerMovimientosConUbicacion() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM movimientos WHERE latitud IS NOT NULL AND longitud IS NOT NULL", null);
    }

    // Obtener todos los contactos
    public Cursor obtenerContactos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM contactos", null);
    }

    // Registrar gasto compartido y dividir deuda entre contactos
    public boolean registrarGastoCompartido(String descripcion, double montoTotal, int contactoId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues valuesGasto = new ContentValues();
            valuesGasto.put("descripcion", descripcion);
            valuesGasto.put("monto_total", montoTotal);
            valuesGasto.put("fecha", "2026-10-29");
            long gastoId = db.insert("gastos_compartidos", null, valuesGasto);

            if (gastoId != -1) {
                // División equitativa (Ejemplo: 50% usuario, 50% contacto)
                double montoDeuda = montoTotal / 2.0;

                ContentValues valuesDeuda = new ContentValues();
                valuesDeuda.put("gasto_id", gastoId);
                valuesDeuda.put("contacto_id", contactoId);
                valuesDeuda.put("monto_asignado", montoDeuda);
                valuesDeuda.put("estado_pago", 0); // 0: Pendiente
                db.insert("detalle_deudas", null, valuesDeuda);

                db.setTransactionSuccessful();
                return true;
            }
        } finally {
            db.endTransaction();
        }
        return false;
    }


    // Obtener el total acumulado que deben todos los contactos
    public double obtenerTotalDeudasAcumuladas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(monto_asignado) FROM detalle_deudas WHERE estado_pago = 0", null);
        double total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getDouble(0);
            cursor.close();
        }
        return total;
    }

    // Obtener el detalle de deudas agrupado por contacto
    public Cursor obtenerDetalleDeudasPorContacto() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT c.nombre, SUM(d.monto_asignado) AS total_deuda " +
                "FROM detalle_deudas d " +
                "INNER JOIN contactos c ON d.contacto_id = c.id " +
                "WHERE d.estado_pago = 0 " +
                "GROUP BY c.id, c.nombre";
        return db.rawQuery(query, null);
    }


}

