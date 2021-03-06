package br.edu.fatecriopreto.centralestagios.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.edu.fatecriopreto.centralestagios.Banco.DBAdapter;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;
import br.edu.fatecriopreto.centralestagios.notificacao;

public class ConfiguracoesActivity extends ActionBarActivity {

    private Toolbar appBar;

    EditText edtSenha;
    EditText edtConfirmaSenha;
    EditText edtTempoNotificacao;
    Button btnSalvar;
    Button btnSalvarTempo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior()-1)) != ConfiguracoesActivity.class)
            variaveisGlobais.setActivityAnterior(ConfiguracoesActivity.class);
        variaveisGlobais.setAlert(ConfiguracoesActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);


        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        edtConfirmaSenha = (EditText) findViewById(R.id.edtConfirmaSenha);
        edtTempoNotificacao = (EditText) findViewById(R.id.edtTempoNotificacao);
        btnSalvarTempo = (Button) findViewById(R.id.btnSalvarTempo);

        DBAdapter db = new DBAdapter(this);
        db.open();
        if(db.retornarNotificacaoTempo() != 0){

            edtTempoNotificacao.setText(String.valueOf(db.retornarNotificacaoTempo()));
        }
        db.close();

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Senha;
                String ConfirmaSenha;

                Senha = edtSenha.getText().toString();
                ConfirmaSenha = edtConfirmaSenha.getText().toString();

                if (!Senha.equals(ConfirmaSenha)) {
                    edtConfirmaSenha.setError("Senhas não conferem");
                    View erro = edtConfirmaSenha;
                    erro.requestFocus();
                } else if(Senha.length() >=6){
                   // String senha;

                   /// senha = edtSenha.getText().toString();

                    final String url = variaveisGlobais.EndIPAPP + "/trocarsenha.aspx?senha=" + Senha + "&rm=" + variaveisGlobais.getUserRm();

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    JsonObjectRequest getRequest =
                            new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {

                                            Log.d("Senha : ", Senha);
                                            //edtSenha.setText(jsonObject.getString("Senha"));
                                            // edtConfirmaSenha.setText(jsonObject.getString("ConfirmaSenha").toString());
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.d("Error.Response", volleyError.getMessage());
                                }
                            });

                    queue.add(getRequest);

                    edtSenha.setText("");
                    edtConfirmaSenha.setText("");
                    Toast.makeText(ConfiguracoesActivity.this, "A senha foi alterada com sucesso", Toast.LENGTH_LONG).show();

                }
                else{
                    edtSenha.setError("Senha tem que ser maior ou igual a 6 digitos");
                    View erro = edtSenha;
                    erro.requestFocus();
                }

            }
            });

        btnSalvarTempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long tempo = Long.parseLong(edtTempoNotificacao.getText().toString());

                if(tempo < 1 || tempo > 120){

                    edtTempoNotificacao.setError("");
                    edtTempoNotificacao.setError("O tempo deve estar entre 1 e 120 minutos!");
                }
                else {

                    DBAdapter db = new DBAdapter(ConfiguracoesActivity.this);
                    db.open();
                    db.zerarNotificacoes();
                    db.adicionarNotificaco(tempo);
                    db.close();

                    notificacao notificacao = new notificacao();
                    notificacao.setDelayOuvidor();

                    Toast.makeText(ConfiguracoesActivity.this, "Intervalo salvo com sucesso!", Toast.LENGTH_LONG).show();
                    Log.d("tempo: ", String.valueOf(notificacao.delayOuvidor));
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_configuracoes, menu);
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
        startActivity(new Intent(this, variaveisGlobais.getActivityAnterior(variaveisGlobais.getSizeActivityAnterior()-2)));
        variaveisGlobais.deleteAnterior();
        this.finish();
    }
}
