package br.edu.fatecriopreto.centralestagios.Banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static String NOME_BANCO = "CentralEstagios.db";
    public static int VERSAO_BANCO = 1;

    public static String RM = "rm";
    public static String CURSOID = "cursoId";
    public static String CIDADE = "cidade";
    public static String TELEFONE = "telefone";
    public static String CEP = "cep";
    public static String ANO = "ano";
    public static String UF = "uf";
    public static String IMAGEM = "imagem";
    public static String BAIRRO = "bairro";
    public static String LOGRADOURO = "logradouro";
    public static String COMPLEMENTO = "complemento";
    public static String NOME = "nome";
    public static String EMAIL = "email";
    public static String SEMESTRE = "semestre";
    public static String CURRICULOPDF = "curriculo_pdf";


    public static String TABELA = "perfil";

    public static String CREATE_DATABASE =
            "create table " + TABELA + " ( " +
                    RM + " integer primary key,"+
                    CURSOID + " integer, " +
                    CIDADE + " text, " +
                    TELEFONE + " text, " +
                    CEP + " text, " +
                    UF + " text, " +
                    IMAGEM + " blob, " +
                    BAIRRO + " text, " +
                    LOGRADOURO + " text, " +
                    COMPLEMENTO + " text, " +
                    NOME + " text, " +
                    EMAIL + " text, " +
                    SEMESTRE + " integer, " +
                    CURRICULOPDF + " blob, " +
                    ANO + " integer );";

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
