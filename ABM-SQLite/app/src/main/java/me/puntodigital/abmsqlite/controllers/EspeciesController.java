package me.puntodigital.abmsqlite.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import me.puntodigital.abmsqlite.AyudanteBaseDeDatos;
import me.puntodigital.abmsqlite.modelos.Especie;


public class EspeciesController {
    private AyudanteBaseDeDatos ayudanteBaseDeDatos;
    private String NOMBRE_TABLA = "especies";

    public EspeciesController(Context contexto) {
        ayudanteBaseDeDatos = new AyudanteBaseDeDatos(contexto);
    }


    public int eliminarEspecie(Especie especie) {

        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        String[] argumentos = {String.valueOf(especie.getId())};
        return baseDeDatos.delete(NOMBRE_TABLA, "id = ?", argumentos);
    }

    public long nuevaEspecie(Especie especie) {
        // writable porque vamos a insertar
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaInsertar = new ContentValues();
        valoresParaInsertar.put("nombre", especie.getNombre());
        valoresParaInsertar.put("altura", especie.getAltura());
        return baseDeDatos.insert(NOMBRE_TABLA, null, valoresParaInsertar);
    }

    public int guardarCambios(Especie especieEditada) {
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getWritableDatabase();
        ContentValues valoresParaActualizar = new ContentValues();
        valoresParaActualizar.put("nombre", especieEditada.getNombre());
        valoresParaActualizar.put("altura", especieEditada.getAltura());
        // where id...
        String campoParaActualizar = "id = ?";
        // ... = idEspecie
        String[] argumentosParaActualizar = {String.valueOf(especieEditada.getId())};
        return baseDeDatos.update(NOMBRE_TABLA, valoresParaActualizar, campoParaActualizar, argumentosParaActualizar);
    }

    public ArrayList<Especie> obtenerEspecies() {
        ArrayList<Especie> especies = new ArrayList<>();
        // readable porque no vamos a modificar, solamente leer
        SQLiteDatabase baseDeDatos = ayudanteBaseDeDatos.getReadableDatabase();
        // SELECT nombre, altura, id
        String[] columnasAConsultar = {"nombre", "altura", "id"};
        Cursor cursor = baseDeDatos.query(
                NOMBRE_TABLA,//from especies
                columnasAConsultar,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            /*
                Salimos aquí porque hubo un error, regresar
                lista vacía
             */
            return especies;

        }
        // Si no hay datos, igualmente regresamos la lista vacía
        if (!cursor.moveToFirst()) return especies;

        // En caso de que sí haya, iteramos y vamos agregando los
        // datos a la lista de especies
        do {
            // El 0 es el número de la columna, como seleccionamos
            // nombre, altura,id entonces el nombre es 0, altura 1 e id es 2
            String nombreObtenidoDeBD = cursor.getString(0);
            int alturaObtenidaDeBD = cursor.getInt(1);
            long idEspecie = cursor.getLong(2);
            Especie especieObtenidaDeBD = new Especie(nombreObtenidoDeBD, alturaObtenidaDeBD, idEspecie);
            especies.add(especieObtenidaDeBD);
        } while (cursor.moveToNext());

        // Fin del ciclo. Cerramos cursor y regresamos la lista de especies :)
        cursor.close();
        return especies;
    }
}