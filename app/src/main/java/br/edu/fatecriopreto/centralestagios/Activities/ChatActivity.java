package br.edu.fatecriopreto.centralestagios.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Entidades.Observacao;
import br.edu.fatecriopreto.centralestagios.Entidades.Vaga;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Utils.ChatAdapter;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class ChatActivity extends ActionBarActivity {

    private Toolbar appBar;
    public String vagaId;
    public String vagaDescricao;
    private ListView listViewChat;
    private ChatAdapter listAdapter;

    private Button btnEviar;
    private EditText edtMsg;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        variaveisGlobais.setAlert(ChatActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        vagaId =  bundle.getString("id");
        vagaDescricao = bundle.getString("titlevaga");
        setTitle(vagaDescricao);

        final String url = variaveisGlobais.EndIPAPP + "/Mensagem.aspx?rm=" + variaveisGlobais.getUserRm() +
                "&acao=listarChat&vaga="+vagaId;

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest getRequest =
                new JsonArrayRequest( url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {

                                if(jsonArray != null) {
                                   List<Observacao> listAuxiliar = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Observacao msg = new Observacao();
                                        try {
                                            msg.setDonoMsg(jsonArray.getJSONObject(i).getString("DonoMsg"));
                                            msg.setDescricao(jsonArray.getJSONObject(i).getString("Descricao"));
                                            listAuxiliar.add(msg);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                        variaveisGlobais.Mensagem = listAuxiliar;

                                    if(variaveisGlobais.Mensagem.size() > 0) {
                                        listViewChat = (ListView) findViewById(R.id.messagesContainer);
                                        listAdapter = new ChatAdapter(ChatActivity.this, variaveisGlobais.Mensagem);
                                        listViewChat.setAdapter(listAdapter);
                                    }

                                    mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
                                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Error.Response", volleyError.getMessage());
                    }
                });

        queue.add(getRequest);

        btnEviar = (Button) findViewById(R.id.chatSendButton);
        edtMsg = (EditText) findViewById(R.id.messageEdit);
        btnEviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtMsg.getText()== null || edtMsg.getText().equals("") || edtMsg.getText().length() == 0){
                    edtMsg.setError("Digite algo");
                    View view = edtMsg;
                    view.requestFocus();
                }else{

                    final String url = variaveisGlobais.EndIPAPP + "/Mensagem.aspx?rm=" + variaveisGlobais.getUserRm() +
                            "&acao=enviarMsg&vaga="+vagaId+"&msg="+ Uri.encode(edtMsg.getText().toString());


                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


                    JsonObjectRequest getRequest =
                            new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {

                                            try {
                                                if(jsonObject.getString("resposta").equals("ok")) {
                                                    Toast.makeText(ChatActivity.this, "Mensagem enviada com sucesso!", Toast.LENGTH_LONG).show();


                                                    Observacao msg = new Observacao();
                                                    msg.setId(Integer.parseInt(jsonObject.getString("idMsg")));
                                                    msg.setAdmId(null);
                                                    msg.setDonoMsg(variaveisGlobais.getUserRm());
                                                    msg.setAlunoId(variaveisGlobais.getUserRm());
                                                    msg.setVagaId(Integer.parseInt(vagaId));
                                                    msg.setDescricao(edtMsg.getText().toString());

                                                    variaveisGlobais.Mensagem.add(msg);
                                                    if(variaveisGlobais.Mensagem.size() > 0) {
                                                        listViewChat = (ListView) findViewById(R.id.messagesContainer);
                                                        listAdapter = new ChatAdapter(ChatActivity.this, variaveisGlobais.Mensagem);
                                                        listViewChat.setAdapter(listAdapter);
                                                    }
                                                    edtMsg.setText("");

                                                }
                                                else {
                                                    Toast.makeText(ChatActivity.this, "Erro ao salvar mensagem, tente mais tarde!", Toast.LENGTH_LONG).show();
                                                    edtMsg.setText("");
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

                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_chat, menu);
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
        startActivity(new Intent(this, MensagemActivity.class));
        this.finish();
    }
}
