package br.edu.fatecriopreto.centralestagios.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Banco.DBAdapter;
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

    //variavel que guarda foto
    Bitmap fotoPerfil;

    Button btnBuscar;
    Button btnSalvar;

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
        spnCurso = (Spinner) findViewById(R.id.spnCurso);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCurso.setAdapter(dataAdapter);


        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnSalvar = (Button)findViewById(R.id.btnSalvar);

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

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //variaveis que recebem os valores das edts
                int rm, cursoId, ano, lembrarRm;
                String nome, email, telefone, cep, logradouro, complemento, bairro, cidade, uf,  semestre;

                rm = Integer.parseInt(variaveisGlobais.getUserRm());

                //atribuicao do valor das edts para as variaveis
                ano = Integer.parseInt(edtAno.getText().toString());
                semestre = edtSemestre.getText().toString();
                cursoId = Integer.parseInt(spnCurso.getSelectedItemId()+"");
                nome = edtNome.getText().toString();
                email = edtEmail.getText().toString();
                telefone = edtTelefone.getText().toString();
                cep = edtCep.getText().toString();
                logradouro = edtLogradouro.getText().toString();
                complemento = edtComplemento.getText().toString();
                bairro = edtBairro.getText().toString();
                cidade = edtCidade.getText().toString();
                uf = edtUf.getText().toString();
                lembrarRm = 0;

                database.adicionar(rm, cursoId, cidade, telefone, cep, ano, uf, bairro, logradouro,
                 complemento, nome, email, semestre, lembrarRm);

                //envia para webservice


                //chama main se tudo ok
                startActivity(new Intent(PerfilRequeridoActivity.this, MainActivity.class));
                variaveisGlobais.deleteAnterior();
                PerfilRequeridoActivity.this.finish();
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
