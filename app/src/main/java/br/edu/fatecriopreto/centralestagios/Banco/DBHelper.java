package br.edu.fatecriopreto.centralestagios.Banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static String NOME_BANCO = "CentralEstagios.db";
    public static int VERSAO_BANCO = 5;

    public static String RM = "rm";
    public static String STATUS = "status";


    public static String TABELARM = "tbRm";

    public static String CREATE_DATABASE_RM =
            "create table " + TABELARM + " ( " +
                    RM + " integer primary key,"+
                    STATUS + " integer );";

    public static String CREATE_DATABASE_NOTIFICACAO =
            "create table notificacoes (" +
                    "tempo integer);";

    public static String DROP_DATABASE_RM =
            " drop table if exists " + TABELARM;

    public static String DROP_DATABASE_NOTIFICACAO =
            " drop table if exists notificacoes;";

    public DBHelper(Context context){
        super(context,NOME_BANCO,null,VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_DATABASE_RM);
        db.execSQL(CREATE_DATABASE_NOTIFICACAO);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_DATABASE_RM);
        db.execSQL(DROP_DATABASE_NOTIFICACAO);
        onCreate(db);
    }
}
