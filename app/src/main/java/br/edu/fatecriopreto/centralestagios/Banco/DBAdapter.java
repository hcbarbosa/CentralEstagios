package br.edu.fatecriopreto.centralestagios.Banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.edu.fatecriopreto.centralestagios.Entidades.Perfil;
import br.edu.fatecriopreto.centralestagios.Entidades.RM;

public class DBAdapter {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    //private String[] colunas = {DBHelper.RM,DBHelper.STATUS};

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

    public void apagarRms(){
        database.execSQL("delete from " + DBHelper.TABELA);
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

    public Cursor getRM(){

        Cursor cursor =

        database.rawQuery(
                " select rm from "
                        + DBHelper.TABELA , null);

        return cursor;
    }

    public RM cursorRm(Cursor cursor){

        RM rm = new RM(cursor.getInt(0), 1);

        return rm;
    }

    public RM retornarRm(){

        Cursor cursor = this.getRM();
        RM rm = new RM(0, 0);

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            while(!cursor.isAfterLast()){

                rm = cursorRm(cursor);
                cursor.moveToNext();
            }
        }

        return rm;
    }

    private RM cursorRM(Cursor cursor){
        return new RM(cursor.getInt(0),cursor.getInt(1));
    }



}
