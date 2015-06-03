package br.edu.fatecriopreto.centralestagios.Banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static String NOME_BANCO = "CentralEstagios.db";
    public static int VERSAO_BANCO = 1;

    public static String RM = "rm";
    public static String STATUS = "status";


    public static String TABELA = "tbRm";

    public static String CREATE_DATABASE =
            "create table " + TABELA + " ( " +
                    RM + " integer primary key,"+
                    STATUS + " integer );";

    public static String DROP_DATABASE =
            " drop table if exists " + TABELA;

    public DBHelper(Context context){
        super(context,NOME_BANCO,null,VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_DATABASE);
        onCreate(db);
    }
}
