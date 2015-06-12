package br.edu.fatecriopreto.centralestagios.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Banco.DBAdapter;
import br.edu.fatecriopreto.centralestagios.Entidades.Curso;
import br.edu.fatecriopreto.centralestagios.Entidades.Perfil;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class PerfilRequeridoActivity extends ActionBarActivity {

    private Toolbar appBar;

    EditText edtCep;
    EditText edtLogradouro;
    EditText edtComplemento;
    EditText edtBairro;
    EditText edtCidade;
    EditText edtUf;
    EditText edtNome;
    EditText edtEmail;
    EditText edtTelefone;
    EditText edtAno;
    EditText edtSemestre;
    Spinner spnCurso;

    public View focusView;

    Button btnBuscar;
    Button btnSalvar;

    int idCurso;

    //objeto de para operacoes em banco de dados
    DBAdapter database = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_requerido);

        //Auxiliar na transicao de telas e pilha
        variaveisGlobais.setAlert(PerfilRequeridoActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        edtCep = (EditText)findViewById(R.id.edtCepOb);
        edtLogradouro = (EditText)findViewById(R.id.edtLogradouroOb);
        edtComplemento = (EditText)findViewById(R.id.edtComplementoOb);
        edtBairro = (EditText)findViewById(R.id.edtBairroOb);
        edtCidade = (EditText)findViewById(R.id.edtCidadeOb);
        edtUf = (EditText)findViewById(R.id.edtUfOb);
        edtNome = (EditText)findViewById(R.id.edtNomeCompletoOb);
        edtEmail = (EditText)findViewById(R.id.edtEmailOb);
        edtTelefone = (EditText)findViewById(R.id.edtTelefoneOb);
        edtAno = (EditText)findViewById(R.id.edtAnoOb);
        edtSemestre = (EditText)findViewById(R.id.edtSemestreOb);
        spnCurso = (Spinner) findViewById(R.id.spnCursosOb);

        focusView = null;

        /*
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCurso.setAdapter(dataAdapter);
        */

        btnBuscar = (Button)findViewById(R.id.btnBuscarOb);
        btnSalvar = (Button)findViewById(R.id.btnSalvarOb);

        //webservice de cep
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cep = edtCep.getText().toString();
                if(!cep.equals("") && !cep.isEmpty()){
                final String url = "http://viacep.com.br/ws/" + cep + "/json/";

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                JsonObjectRequest getRequest =
                        new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        try {

                                            edtLogradouro.setText(jsonObject.getString("logradouro"));
                                            edtBairro.setText(jsonObject.getString("bairro"));
                                            edtCidade.setText(jsonObject.getString("localidade"));
                                            edtUf.setText(jsonObject.getString("uf"));

                                        } catch (JSONException ex){
                                            ex.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.d("Error.Response", volleyError.getMessage());
                            }
                        });

                queue.add(getRequest);
                }else{
                    edtCep.setError("Digite um cep corretamente");
                    View focus = edtCep;
                    focus.requestFocus();
                }
            }
        });

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle("Realizar cadastro?");
        alertDialog.setIcon(R.drawable.app_icon);
        alertDialog.setMessage("Deseja realmente realizar este cadastro?\n" +
                "Verifique os valores de ano, semestre e o curso pois não poderão" +
                " ser modificados posteriormente!");

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "FECHAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                alertDialog.dismiss();
            }
        });


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtNome.getText().toString().equals("")) {
                    focusView = edtNome;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else if (edtEmail.getText().toString().equals("")) {
                    focusView = edtEmail;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else if (edtTelefone.getText().toString().equals("")) {
                    focusView = edtTelefone;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else if (edtAno.getText().toString().equals("")) {
                    focusView = edtAno;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else if (edtSemestre.getText().toString().equals("")) {
                    focusView = edtSemestre;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else if (edtCep.getText().toString().equals("")) {
                    focusView = edtCep;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else if (edtLogradouro.getText().toString().equals("")) {
                    focusView = edtLogradouro;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else if (edtBairro.getText().toString().equals("")) {
                    focusView = edtBairro;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else if (edtCidade.getText().toString().equals("")) {
                    focusView = edtCidade;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else if (edtUf.getText().toString().equals("")) {
                    focusView = edtUf;
                    ((EditText)focusView).setError("Campo obrigatório!");
                    focusView.requestFocus();

                } else {

                    //variaveis que recebem os valores das edts
                    final int rm, cursoId, ano, lembrarRm;
                    final String nome, email, telefone, cep, logradouro, complemento, bairro, cidade, uf, semestre;

                    rm = Integer.parseInt(variaveisGlobais.getUserRm());

                    //atribuicao do valor das edts para as variaveis
                    ano = Integer.parseInt(edtAno.getText().toString());
                    semestre = edtSemestre.getText().toString();
                    nome = Uri.encode(edtNome.getText().toString());
                    email = Uri.encode(edtEmail.getText().toString());
                    telefone = Uri.encode(edtTelefone.getText().toString());
                    cep = Uri.encode(edtCep.getText().toString());
                    logradouro = Uri.encode(edtLogradouro.getText().toString());
                    complemento = Uri.encode(edtComplemento.getText().toString());
                    bairro = Uri.encode(edtBairro.getText().toString());
                    cidade = Uri.encode(edtCidade.getText().toString());
                    uf = Uri.encode(edtUf.getText().toString());
                    cursoId = idCurso;
                    lembrarRm = 0;

                    //envia para webservice
                    final String url = variaveisGlobais.EndIPAPP + "/primeiroacesso.aspx?ano=" + ano + "&semestre=" + semestre + "&nome="
                            + nome + "&email=" + email + "&telefone=" + telefone + "&cep=" + cep + "&logradouro=" + logradouro + "&complemento="
                            + complemento + "&bairro=" + bairro + "&cidade=" + cidade + "&uf=" + uf + "&rm=" + variaveisGlobais.getUserRm() +
                            "&curso=" + cursoId;

                    //webservice que atualiza perfil
                    final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    final JsonObjectRequest getRequest =
                            new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {

                                            try {
                                                if (jsonObject.getString("Conteudo").equals("ok")) {
                                                    Toast.makeText(PerfilRequeridoActivity.this, "Perfil salvo com sucesso!", Toast.LENGTH_LONG).show();
                                                    variaveisGlobais.setUserName(Uri.decode(nome));
                                                    variaveisGlobais.setUserEmail(Uri.decode(email));
                                                } else {
                                                    Toast.makeText(PerfilRequeridoActivity.this, "Erro ao salvar o perfil, tente mais tarde!", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.d("Error.Response", "erro");
                                }
                            });

                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CADASTRAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            queue.add(getRequest);

                            Perfil perfilAtualizado = new Perfil();

                            perfilAtualizado.setAno(ano);
                            perfilAtualizado.setBairro(Uri.decode(bairro));
                            perfilAtualizado.setCEP(Uri.decode(cep));
                            perfilAtualizado.setCidade(Uri.decode(cidade));
                            perfilAtualizado.setComplemento(Uri.decode(complemento));
                            perfilAtualizado.setCursoId(cursoId);
                            perfilAtualizado.setEmail(Uri.decode(email));
                            perfilAtualizado.setLogradouro(Uri.decode(logradouro));
                            perfilAtualizado.setNome(Uri.decode(nome));
                            variaveisGlobais.setUserName(Uri.decode(nome));
                            variaveisGlobais.setUserEmail(Uri.decode(email));
                            perfilAtualizado.setSemestre(String.valueOf(semestre));
                            perfilAtualizado.setTelefone(Uri.decode(telefone));
                            perfilAtualizado.setUf(Uri.decode(uf));

                            variaveisGlobais.perfilRm = perfilAtualizado;

                            //chama main se tudo ok
                            startActivity(new Intent(PerfilRequeridoActivity.this, MainActivity.class));
                            variaveisGlobais.deleteAnterior();
                            PerfilRequeridoActivity.this.finish();
                        }
                    });

                    alertDialog.show();
                }
            }
        });

        if(variaveisGlobais.listCursos == null) {

            variaveisGlobais.listCursos = new ArrayList<>();

            final String url = variaveisGlobais.EndIPAPP + "/cursos.aspx";

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            JsonArrayRequest getRequest =
                    new JsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    Curso curso;

                                    for (int i = 0; i < response.length(); i++) {

                                        curso = new Curso();

                                        try {
                                            curso.setDescricao(response.getJSONObject(i).getString("Descricao"));
                                            curso.setId(response.getJSONObject(i).getInt("Id"));

                                            variaveisGlobais.listCursos.add(curso);

                                            Log.d("Curso: ", curso.getDescricao() + curso.getId());

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    String[] nomeCursos = new String[variaveisGlobais.listCursos.size()];

                                    int i = 0;

                                    for(Curso aux : variaveisGlobais.listCursos) {

                                        nomeCursos[i] = aux.getDescricao();

                                        idCurso = variaveisGlobais.listCursos.get(i).getId();

                                        i++;
                                    }

                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PerfilRequeridoActivity.this, android.R.layout.simple_spinner_item, nomeCursos);

                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                                    spnCurso.setAdapter(arrayAdapter);

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("Error.Response", volleyError.getMessage());
                        }
                    });

            queue.add(getRequest);
        }
        else {

            String[] nomeCursos = new String[variaveisGlobais.listCursos.size()];

            int i = 0;

            for (Curso aux : variaveisGlobais.listCursos) {

                nomeCursos[i] = aux.getDescricao();

                idCurso = variaveisGlobais.listCursos.get(i).getId();

                i++;
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomeCursos);

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            spnCurso.setAdapter(arrayAdapter);
        }

        spnCurso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                idCurso = variaveisGlobais.listCursos.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_perfil_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Pega o evento de voltar do celular e volta para a activity anterior
    public void onBackPressed(){
        startActivity(new Intent(this, LoginActivity.class));
        variaveisGlobais.deleteAnterior();
        this.finish();
    }
}
