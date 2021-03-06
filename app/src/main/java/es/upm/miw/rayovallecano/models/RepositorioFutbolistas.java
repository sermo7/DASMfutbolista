package es.upm.miw.rayovallecano.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static es.upm.miw.rayovallecano.models.FutbolistaContract.tablaFutbolista;

public class RepositorioFutbolistas extends SQLiteOpenHelper {

    private static final String DATABASE_FILENAME = "futbolistas.db";

    private static final int DATABASE_VERSION = 1;


    public RepositorioFutbolistas(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String sentenciaSQL = "CREATE TABLE "+FutbolistaContract.tablaFutbolista.TABLE_NAME + "("; //como queda muy mal hacer import directamente (el de tablaFutbolista y la linea queda mas pequeña
        String sentenciaSQL = "CREATE TABLE " + tablaFutbolista.TABLE_NAME + "(" //el espacio después de TABLE es importante al igual que abajo el de antes de INTEGER para evitar que se concatene
                + tablaFutbolista.COL_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + tablaFutbolista.COL_NAME_NOMBRE + " TEXT, "
                + tablaFutbolista.COL_NAME_DORSAL + " INTEGER, "
                + tablaFutbolista.COL_NAME_LESIONADO + " INTEGER, " //Se pone INTEGER porque no hay booelanos. Utilizar 0 o 1
                + tablaFutbolista.COL_NAME_EQUIPO + " TEXT, "
                + tablaFutbolista.COL_NAME_URL + " TEXT)";
        db.execSQL(sentenciaSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sentenciaSQL = "DROP TABLE IF EXISTS " + tablaFutbolista.TABLE_NAME;
        db.execSQL(sentenciaSQL);
        onCreate(db); //Para que haga una limpia de la tabla
    }


    public long add(Futbolista futbolista) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put(tablaFutbolista.COL_NAME_ID, futbolista.get_id()); //Los ID realmente no hace falta pasarlos.
        valores.put(tablaFutbolista.COL_NAME_NOMBRE, futbolista.get_nombre());
        valores.put(tablaFutbolista.COL_NAME_DORSAL, futbolista.get_dorsal());
        valores.put(tablaFutbolista.COL_NAME_LESIONADO, futbolista.is_lesionado());
        valores.put(tablaFutbolista.COL_NAME_EQUIPO, futbolista.get_equipo());
        valores.put(tablaFutbolista.COL_NAME_URL, futbolista.get_url_imagen());

        return db.insert(tablaFutbolista.TABLE_NAME, null, valores);
    }

    public ArrayList<Futbolista> getAll() {
        ArrayList<Futbolista> futbolistas = new ArrayList<>();
        String consultaSQL = "SELECT * FROM " + tablaFutbolista.TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase();

        // hacer consulta y recuperar información

        Cursor cursor = db.rawQuery(consultaSQL, null); //Devuelve un cursor en todas las filas que devolverá de la columna

        if (cursor.moveToFirst()) { //si hay  datos (no hace falta preguntar si es vacío)
            while (!cursor.isAfterLast()) {
                Futbolista futbolista = new Futbolista(
                        cursor.getInt(cursor.getColumnIndex(tablaFutbolista.COL_NAME_ID)), //En lugar de poner 0 que dependería de cómo estuviera colocado entonces se selecciona a través del nombre de la columna.
                        cursor.getString(cursor.getColumnIndex(tablaFutbolista.COL_NAME_NOMBRE)),
                        cursor.getInt(cursor.getColumnIndex(tablaFutbolista.COL_NAME_DORSAL)),
                        cursor.getInt(cursor.getColumnIndex(tablaFutbolista.COL_NAME_LESIONADO)) != 0, //Al ser booleano pregunto si el valor es distinto de 0
                        cursor.getString(cursor.getColumnIndex(tablaFutbolista.COL_NAME_EQUIPO)),
                        cursor.getString(cursor.getColumnIndex(tablaFutbolista.COL_NAME_URL))
                );

                futbolistas.add(futbolista);
                cursor.moveToNext();

            }

        }


        return futbolistas;
    }


}
