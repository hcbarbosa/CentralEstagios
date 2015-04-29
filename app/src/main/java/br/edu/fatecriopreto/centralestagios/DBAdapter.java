package br.edu.fatecriopreto.centralestagios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Arian on 28/04/2015.
 */
public class DBAdapter {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] colunas = {DBHelper.RM,DBHelper.CURSOID,
            DBHelper.CIDADE, DBHelper.TELEFONE, DBHelper.CEP,
            DBHelper.ANO, DBHelper.UF,DBHelper.IMAGEM,DBHelper.BAIRRO,
            DBHelper.LOGRADOURO,DBHelper.COMPLEMENTO,DBHelper.NOME,
            DBHelper.EMAIL,DBHelper.SEMESTRE,DBHelper.CURRICULOPDF};

    public  DBAdapter(Context context){
        dbHelper = new DBHelper(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
    }

    public void adicionar(Integer rm,
                          Integer cursoId, String cidade, String telefone, String cep,
                          int ano, String uf, String bairro, String logradouro, String complemento,
                          String nome, String email, String semestre ) {
        ContentValues contentValues =
                new ContentValues();

        contentValues.put(DBHelper.RM, rm);
        contentValues.put(DBHelper.CURSOID, cursoId);
        contentValues.put(DBHelper.CIDADE, cidade);
        contentValues.put(DBHelper.TELEFONE, telefone);
        contentValues.put(DBHelper.CEP, cep);
        contentValues.put(DBHelper.ANO, ano);
        contentValues.put(DBHelper.UF, uf);
        contentValues.put(DBHelper.BAIRRO, bairro);
        contentValues.put(DBHelper.LOGRADOURO, logradouro);
        contentValues.put(DBHelper.COMPLEMENTO, complemento);
        contentValues.put(DBHelper.NOME, nome);
        contentValues.put(DBHelper.EMAIL, email);
        contentValues.put(DBHelper.SEMESTRE, semestre);

        database.insert(DBHelper.TABELA, null,
                contentValues);
    }


}
