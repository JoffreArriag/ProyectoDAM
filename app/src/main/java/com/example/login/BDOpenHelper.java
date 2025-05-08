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
    public static final int bdversion = 3;

    public BDOpenHelper(Context context) {
        super(context, bdName, null, bdversion);
    }
    public static final String tablausuario = "CREATE TABLE usuario(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
        "cedula TEXT," +
        "nombres TEXT, "+
        "apellidos TEXT, "+
        "edad TEXT, "+
        "nacionalidad TEXT, "+
        "genero TEXT, "+
        "estado_civil TEXT, "+
        "fecha_nacimiento TEXT, "+
        "ratingIngles FLOAT)";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla antigua "usuarios" si existe
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        // Eliminar la tabla "usuario" si existe (por si necesitamos recrearla)
        db.execSQL("DROP TABLE IF EXISTS usuario");
        // Eliminar la tabla cultivos para recrearla
        db.execSQL("DROP TABLE IF EXISTS cultivos");
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
                "ubicacion TEXT)";
        db.execSQL(crearTablaCultivos);
    }





















    public boolean insertarPersona(String cedula, String nombres, String apellidos, String edad,
                                   String nacionalidad, String genero, String estadoCivil,
                                   String fechaNacimiento, float nivelIngles) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("cedula", cedula);
        contentValues.put("nombres", nombres);
        contentValues.put("apellidos", apellidos);
        contentValues.put("edad", edad);
        contentValues.put("nacionalidad", nacionalidad);
        contentValues.put("genero", genero);
        contentValues.put("estado_civil", estadoCivil);
        contentValues.put("fecha_nacimiento", fechaNacimiento);
        contentValues.put("nivel_ingles", nivelIngles);

        long resultado = db.insert("usuarios", null, contentValues);
        db.close();
        return resultado != -1;
    }

    public boolean insertarCultivo(Cultivo cultivo) {
        SQLiteDatabase db = this.getWritableDatabase();


        Cursor cursor = db.rawQuery(
                "SELECT * FROM cultivos WHERE nombre = ? AND fecha_inicio = ?",
                new String[]{cultivo.getNombre(), cultivo.getFechaInicio()}
        );

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false;
        }

        ContentValues valores = new ContentValues();
        valores.put("nombre", cultivo.getNombre());
        valores.put("categoria", cultivo.getCategoria());
        valores.put("fecha_inicio", cultivo.getFechaInicio());
        valores.put("ubicacion", cultivo.getUbicacion());

        long resultado = db.insert("cultivos", null, valores);

        cursor.close();
        db.close();

        return resultado != -1;
    }


    public List<Cultivo> obtenerTodosLosCultivos() {
        List<Cultivo> listaCultivos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM cultivos", null);

        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));
                String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio"));
                String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"));

                Cultivo cultivo = new Cultivo(nombre, categoria, fechaInicio, ubicacion);
                listaCultivos.add(cultivo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaCultivos;
    }

    public List<Cultivo> buscarCultivosPorNombreOUbicacion(String criterio) {
        List<Cultivo> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cultivos WHERE nombre LIKE ? OR ubicacion LIKE ?", new String[]{"%" + criterio + "%", "%" + criterio + "%"});

        if (cursor.moveToFirst()) {
            do {
                Cultivo cultivo = new Cultivo(
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")),
                        cursor.getString(cursor.getColumnIndexOrThrow("categoria"))
                );
                lista.add(cultivo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // NUEVO: Metodo para actualizar un cultivo
    public boolean actualizarCultivo(Cultivo cultivoAntiguo, Cultivo cultivoNuevo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("nombre", cultivoNuevo.getNombre());
        valores.put("categoria", cultivoNuevo.getCategoria());
        valores.put("fecha_inicio", cultivoNuevo.getFechaInicio());
        valores.put("ubicacion", cultivoNuevo.getUbicacion());


        int resultado = db.update("cultivos", valores, "nombre = ? AND categoria = ? AND fecha_inicio = ?",
                new String[]{cultivoAntiguo.getNombre(), cultivoAntiguo.getCategoria(), cultivoAntiguo.getFechaInicio()});

        db.close();
        return resultado > 0;
    }



    // NUEVO: Método para eliminar un cultivo
    public boolean eliminarCultivo(Cultivo cultivo) {
        SQLiteDatabase db = this.getWritableDatabase();

        int resultado = db.delete("cultivos", "nombre = ? AND fecha_inicio = ?",
                new String[]{cultivo.getNombre(), cultivo.getFechaInicio()});

        db.close();
        return resultado > 0;
    }
}
