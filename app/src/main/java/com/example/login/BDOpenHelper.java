package com.example.login;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class BDOpenHelper extends SQLiteOpenHelper {

    public static final String bdName = "agricola.sqlite";
    public static final int bdversion = 9;

    public BDOpenHelper(Context context) {
        super(context, bdName, null, bdversion);
    }
    public static final String tablausuario = "CREATE TABLE usuario(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "cedula TEXT UNIQUE," +
            "nombres TEXT, "+
            "apellidos TEXT, "+
            "edad INTEGER, "+
            "nacionalidad TEXT, "+
            "genero TEXT, "+
            "estado_civil TEXT, "+
            "fecha_nacimiento TEXT, "+
            "ratingIngles FLOAT)";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla "usuario" si existe
        db.execSQL("DROP TABLE IF EXISTS usuario");
        // Eliminar otras tablas si es necesario
        db.execSQL("DROP TABLE IF EXISTS cultivos");
        db.execSQL("DROP TABLE IF EXISTS ventas");
        db.execSQL("DROP TABLE IF EXISTS agricultores");
        db.execSQL("DROP TABLE IF EXISTS insumosagricola");
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(tablausuario);

        String crearTablaCultivos = "CREATE TABLE cultivos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "categoria TEXT, " +
                "fecha_inicio TEXT, " +
                "ubicacion TEXT," +
                "precio_caja REAL)";
        db.execSQL(crearTablaCultivos);

        String crearTablaVentas = "CREATE TABLE ventas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "cedula TEXT, " +
                "nombre TEXT, " +
                "productos TEXT," +
                "total REAL)";
        db.execSQL(crearTablaVentas);

        String crearTablaAgricultores = "CREATE TABLE agricultores (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "edad INTEGER, " +
                "zona TEXT, " +
                "experiencia TEXT)";
        db.execSQL(crearTablaAgricultores);

        String crearTablaInsumoAgricola = "CREATE TABLE IF NOT EXISTS insumos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "descripcion TEXT, " +
                "cantidad INTEGER)";
        db.execSQL(crearTablaInsumoAgricola);

    }
}
