package br.edu.fatecriopreto.centralestagios.Banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.edu.fatecriopreto.centralestagios.Entidades.Perfil;
import br.edu.fatecriopreto.centralestagios.Entidades.RM;

public class DBAdapter {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] colunas = {DBHelper.RM,DBHelper.STATUS};

    public  DBAdapter(Context context){
        dbHelper = new DBHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public void refreshdb(){
        database.execSQL(dbHelper.DROP_DATABASE);
        dbHelper.onCreate(database);
    }

    public void adicionar(Integer rm,
                          Integer status) {
        ContentValues contentValues =
                new ContentValues();

        contentValues.put(DBHelper.RM, rm);
        contentValues.put(DBHelper.STATUS, status);

        database.insert(DBHelper.TABELA, null,
                contentValues);
    }

    public void apagar(long rm)
    {
        database.delete(DBHelper.TABELA, DBHelper.RM + " = " + rm, null);
    }

    public Cursor getRM(){
        return database.rawQuery(
                " select rm, status from "
                        + DBHelper.TABELA + "where status = 1", null);
    }

    public Cursor getCursorRM(){
        return database.rawQuery(
                " select rm from "
                        + DBHelper.TABELA, null);
    }

    private RM cursorRM(Cursor cursor){
        return new RM(cursor.getLong(0),cursor.getInt(1));
    }


    public void editarRM(RM rm){
        ContentValues valores =
                new ContentValues();

        valores.put(DBHelper.RM, rm.getRm());
        valores.put(DBHelper.STATUS, rm.getStatus());

        database.update(DBHelper.TABELA,valores,
                DBHelper.RM + " = ?", new String[] {String.valueOf(rm.getRm())});

    }




}
