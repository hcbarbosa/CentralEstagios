package br.edu.fatecriopreto.centralestagios.Activities;

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
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import br.edu.fatecriopreto.centralestagios.Banco.DBAdapter;
import br.edu.fatecriopreto.centralestagios.Entidades.Curso;
import br.edu.fatecriopreto.centralestagios.Entidades.Perfil;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class PerfilEditActivity extends ActionBarActivity {

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

    Spinner spnCursos;

    //variavel que guarda foto
    Bitmap fotoPerfil;

    Button btnBuscar;
    Button btnSalvar;

    //variavel usada para guardar id de spinner de cursos
    int idCurso;

    String url;

    //objeto de para operacoes em banco de dados
    DBAdapter database = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_edit);


        //Auxiliar na transicao de telas e pilha
        variaveisGlobais.setAlert(PerfilEditActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);

        edtCep = (EditText)findViewById(R.id.edtCep);
        edtLogradouro = (EditText)findViewById(R.id.edtLogradouro);
        edtComplemento = (EditText)findViewById(R.id.edtComplemento);
        edtBairro = (EditText)findViewById(R.id.edtBairro);
        edtCidade = (EditText)findViewById(R.id.edtCidade);
        edtUf = (EditText)findViewById(R.id.edtUf);
        edtNome = (EditText)findViewById(R.id.edtNomeCompleto);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtTelefone = (EditText)findViewById(R.id.edtTelefone);
        edtAno = (EditText)findViewById(R.id.edtAno);
        edtSemestre = (EditText)findViewById(R.id.edtSemestre);

        spnCursos = (Spinner) findViewById(R.id.spnCursos);

        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnSalvar = (Button)findViewById(R.id.btnSalvar);

       //-------------------------------------------------------------------------------------
        final Perfil perfil = variaveisGlobais.perfilRm;


        final long cursoId = perfil.getCursoId(), ano = perfil.getAno(), semestre = Integer.parseInt(perfil.getSemestre());
        String nome = perfil.getNome(), email = perfil.getEmail(), telefone = perfil.getTelefone(),
                cep = perfil.getCEP(), logradouro = perfil.getLogradouro(), complemento = perfil.getComplemento(),
                bairro = perfil.getBairro(), cidade = perfil.getCidade(), uf = perfil.getUf();

        edtAno.setText(String.valueOf(ano));
        edtSemestre.setText(String.valueOf(semestre));
        edtNome.setText(nome);
        edtEmail.setText(email);
        edtTelefone.setText(telefone);
        edtCep.setText(cep);
        edtLogradouro.setText(logradouro);
        edtComplemento.setText(complemento);
        edtBairro.setText(bairro);
        edtCidade.setText(cidade);
        edtUf.setText(uf);
        //-----------------------------------------------------------------------------------

        //webservice de cep
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cep = edtCep.getText().toString();

                if (cep != null && !cep.isEmpty()) {
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

                                            } catch (JSONException ex) {
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
                    edtCep.setError(getResources().getString(R.string.cep_requerido));
                    View vFocus = edtCep;
                    vFocus.requestFocus();
                }
            }
        });
      //-------------------------------------------------------------------------------------------

        spnCursos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                idCurso = variaveisGlobais.listCursos.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //variaveis que recebem os valores das edts
                int  cursoId, ano, semestre, lembrarRm;
                final String nome, email, telefone, cep, logradouro, complemento, bairro, cidade, uf;

                //atribuição do valor das edts para as variaveis codificando-as para que a url de requisição as entenda
                ano = Integer.parseInt(edtAno.getText().toString());
                semestre = Integer.parseInt(edtSemestre.getText().toString());
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

                url = variaveisGlobais.EndIPAPP + "/perfil.aspx?ano=" + ano + "&semestre=" + semestre + "&nome="
                        + nome + "&email=" + email + "&telefone=" + telefone + "&cep=" + cep + "&logradouro=" + logradouro + "&complemento="
                        + complemento + "&bairro=" + bairro + "&cidade=" + cidade + "&uf=" + uf + "&rm=" + variaveisGlobais.getUserRm() +
                        "&curso=" + cursoId + "&acao=atualizar";

                //webservice que atualiza perfil
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                JsonObjectRequest getRequest =
                        new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {

                                        try {
                                            if(jsonObject.getString("Conteudo").equals("ok")) {
                                                Toast.makeText(PerfilEditActivity.this, "Perfil salvo com sucesso!", Toast.LENGTH_LONG).show();
                                                variaveisGlobais.setUserName(nome);
                                                variaveisGlobais.setUserEmail(email);
                                            }
                                            else {
                                                Toast.makeText(PerfilEditActivity.this, "Erro ao salvar o perfil, tente mais tarde!", Toast.LENGTH_LONG).show();
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

                queue.add(getRequest);

                //Toast.makeText(PerfilEditActivity.this, "Perfil salvo com sucesso!", Toast.LENGTH_LONG).show();

                //atualizando os dados da variavel global perfil com os novos que o usuario atualizou
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
                perfilAtualizado.setSemestre(String.valueOf(semestre));
                perfilAtualizado.setTelefone(Uri.decode(telefone));
                perfilAtualizado.setUf(Uri.decode(uf));

                variaveisGlobais.perfilRm = perfilAtualizado;

                startActivity(new Intent(PerfilEditActivity.this, PerfilActivity.class));
                PerfilEditActivity.this.finish();
            }
        });

        //--------------------------------------------------------------------------------
        // Populando spinner de cursos

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
                                    int cursoPosition = 0;

                                    for(Curso aux : variaveisGlobais.listCursos) {

                                        nomeCursos[i] = aux.getDescricao();

                                        if (variaveisGlobais.listCursos.get(i).getId() == variaveisGlobais.perfilRm.getCursoId()) {

                                            cursoPosition = i;
                                        }

                                        idCurso = variaveisGlobais.listCursos.get(i).getId();

                                        i++;
                                    }

                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PerfilEditActivity.this, android.R.layout.simple_spinner_item, nomeCursos);

                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                                    spnCursos.setEnabled(false);
                                    spnCursos.setClickable(false);
                                    spnCursos.setAdapter(arrayAdapter);

                                    spnCursos.setSelection(cursoPosition);
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
            int cursoPosition = 0;

            for (Curso aux : variaveisGlobais.listCursos) {

                nomeCursos[i] = aux.getDescricao();

                if (variaveisGlobais.listCursos.get(i).getId() == variaveisGlobais.perfilRm.getCursoId()) {

                    cursoPosition = i;
                }

                idCurso = variaveisGlobais.listCursos.get(i).getId();

                i++;
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomeCursos);

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            spnCursos.setAdapter(arrayAdapter);

            spnCursos.setSelection(cursoPosition);
        }
    }
 //-----------------------------------------------------------------------------------------------
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
        startActivity(new Intent(this, PerfilActivity.class));
        variaveisGlobais.deleteAnterior();
        this.finish();
    }
}
