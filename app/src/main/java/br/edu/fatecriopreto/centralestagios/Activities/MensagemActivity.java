package br.edu.fatecriopreto.centralestagios.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.Entidades.Observacao;
import br.edu.fatecriopreto.centralestagios.Entidades.Vaga;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Utils.ListConhecimentosAdapter;
import br.edu.fatecriopreto.centralestagios.Utils.ListMensagemAdapter;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

import static br.edu.fatecriopreto.centralestagios.Activities.VagaActivity.convertDate;

public class MensagemActivity extends ActionBarActivity {

    private ListView listViewMensagens;
    private ListMensagemAdapter listAdapter;
    private Toolbar appBar;
    List<Vaga> listaRooms = new ArrayList<>();
    ImageView btnExcluirMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior()-1)) != MensagemActivity.class)
            variaveisGlobais.setActivityAnterior(MensagemActivity.class);
        variaveisGlobais.setAlert(MensagemActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);

        listViewMensagens = (ListView) findViewById(R.id.roomsList);
        listViewMensagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                Bundle params = new Bundle();
                params.putString("titlevaga", listaRooms.get(position).getDescricao());
                params.putString("id", String.valueOf(listaRooms.get(position).getId()));
                intent.putExtras(params);
                startActivity(intent);
                finish();
            }
        });

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle("Excluir Chat?");
        alertDialog.setIcon(R.drawable.app_icon);
        alertDialog.setMessage("Deseja realmente excluir este chat?");

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "FECHAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                alertDialog.dismiss();
            }
        });

        listViewMensagens.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int pos = position;
                final View v = view;

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "EXCLUIR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String url = variaveisGlobais.EndIPAPP + "/ExcluirChat.aspx?rm=" + variaveisGlobais.getUserRm() +
                                "&vaga=" + String.valueOf(listaRooms.get(pos).getId());

                        final RequestQueue queueExcluir = Volley.newRequestQueue(getApplicationContext());

                        final JsonObjectRequest getRequest =
                                new JsonObjectRequest(Request.Method.GET, url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject jsonObject) {

                                                try {
                                                    if (jsonObject.getString("Conteudo").equals("ok")) {

                                                        Toast.makeText(MensagemActivity.this, "Chat excluido com sucesso!", Toast.LENGTH_LONG).show();
                                                        listViewMensagens.removeViewInLayout(v);
                                                    } else {
                                                        Toast.makeText(MensagemActivity.this, "Erro ao excluir chat, tente mais tarde!", Toast.LENGTH_LONG).show();
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

                        queueExcluir.add(getRequest);
                    }
                });

                alertDialog.show();

                return false;
            }
        });

        final String url = variaveisGlobais.EndIPAPP + "/Mensagem.aspx?rm=" + variaveisGlobais.getUserRm() +
                "&acao=listarRooms";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest getRequest =
                new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {

                                if(jsonArray != null) {
                                        Gson gson = new Gson();

                                    AuxListaVaga auxiliar = null;
                                    try {
                                        auxiliar = gson.fromJson(jsonArray.get(0).toString(), AuxListaVaga.class);
                                        listaRooms.addAll(auxiliar.listaVaga);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Collections.sort(listaRooms, new Comparator<Vaga>() {
                                        @Override
                                        public int compare(Vaga lhs, Vaga rhs) {
                                            return lhs.getDescricao().compareToIgnoreCase(rhs.getDescricao());
                                        }
                                    });
                                    variaveisGlobais.listRooms = listaRooms;
                                    variaveisGlobais.listqdt = auxiliar.listaQtd;

                                    listAdapter = new ListMensagemAdapter(variaveisGlobais.listRooms, MensagemActivity.this);
                                    listViewMensagens.setAdapter(listAdapter);

                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Error.Response", volleyError.getMessage());
                    }
                });

        queue.add(getRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_mensagem, menu);
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

    public class AuxListaVaga{
        public List<Vaga> listaVaga;
        public List<Qtd> listaQtd;
    }

    public class  Qtd{
        public int idVaga;
        public int qtd;
    }


}
