package br.edu.fatecriopreto.centralestagios.Banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.edu.fatecriopreto.centralestagios.Entidades.Perfil;

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

    public void apagar(long idPerfil)
    {
        database.delete(DBHelper.TABELA, DBHelper.RM + " = " + idPerfil, null);
    }

    public Cursor getPerfil(){
        return database.rawQuery(
                " select rm, cursoId, cidade, telefone, cep, ano, uf, bairro, logradouro, complemento, nome, email, semestre from "
                        + DBHelper.TABELA, null);
    }

    private Perfil cursorPerfil(Cursor cursor){
        return new Perfil(cursor.getLong(0),cursor.getLong(1),cursor.getString(3),
                        cursor.getString(4),cursor.getString(5),cursor.getLong(6),cursor.getString(7),
                        cursor.getString(8),cursor.getString(9),cursor.getString(10),
                        cursor.getString(11),cursor.getString(12));
    }

    public Perfil getPerfil(long rm){
        Cursor cursor =
                database.query(DBHelper.TABELA,
                        colunas, DBHelper.RM + " = " +
                                rm, null,null, null, null);

        cursor.moveToFirst();
        return cursorPerfil(cursor);
    }

    /* Nao precisa listar perfis no app
    public List<Perfil> listarPerfil(){
        Cursor cursor = this.getPerfil();
        List<Perfil> lista = new ArrayList<>();

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            Perfil perfil;

            while(!cursor.isAfterLast()){
                perfil = cursorPerfil(cursor);
                lista.add(perfil);
                cursor.moveToNext();
            }
        }
        return lista;
    }
    */

    public void editarPerfil(Perfil perfil){
        ContentValues valores =
                new ContentValues();

        valores.put(DBHelper.RM, perfil.getRm());
        valores.put(DBHelper.CURSOID, perfil.getCursoId());
        valores.put(DBHelper.CIDADE, perfil.getCidade());
        valores.put(DBHelper.ANO, perfil.getAno());
        valores.put(DBHelper.CEP, perfil.getCEP());
        valores.put(DBHelper.TELEFONE, perfil.getTelefone());
        valores.put(DBHelper.UF, perfil.getUf());
        valores.put(DBHelper.BAIRRO, perfil.getBairro());
        valores.put(DBHelper.LOGRADOURO, perfil.getLogradouro());
        valores.put(DBHelper.COMPLEMENTO, perfil.getComplemento());
        valores.put(DBHelper.NOME, perfil.getNome());
        valores.put(DBHelper.EMAIL, perfil.getEmail());
        valores.put(DBHelper.SEMESTRE, perfil.getSemestre());

        database.update(DBHelper.TABELA,valores,
                DBHelper.RM + " = ?", new String[] {String.valueOf(perfil.getRm())});

    }




}
