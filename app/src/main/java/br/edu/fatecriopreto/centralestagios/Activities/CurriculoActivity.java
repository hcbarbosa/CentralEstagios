package br.edu.fatecriopreto.centralestagios.Activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.Collections2;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Utils.ListConhecimentosAdapter;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class CurriculoActivity extends ActionBarActivity {

    private ListView listViewConhecimentos;
    private ListConhecimentosAdapter listAdapter;
    private Toolbar appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculo);


        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior()-1)) != CurriculoActivity.class)
            variaveisGlobais.setActivityAnterior(CurriculoActivity.class);
        variaveisGlobais.setAlert(CurriculoActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);

        final String url = variaveisGlobais.EndIPAPP + "/Curriculo.aspx?rm=" + variaveisGlobais.getUserRm() +
                "&acao=listar";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest getRequest =
                new JsonArrayRequest( url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonObject) {
                                try {

                                    listViewConhecimentos = (ListView) findViewById(R.id.listViewConhecimento);


                                    if(jsonObject != null) {
                                        Gson gson = new Gson();
                                        ConhecimentoList listaConhecimento = gson.fromJson(jsonObject.getString(0), ConhecimentoList.class);
                                        variaveisGlobais.listConhecimentoCurso = listaConhecimento.conhecimentosCurso;
                                        Collections.sort(variaveisGlobais.listConhecimentoCurso, new Comparator<Conhecimento>() {
                                            @Override
                                            public int compare(Conhecimento lhs, Conhecimento rhs) {
                                                return  lhs.getDescricao().compareToIgnoreCase(rhs.getDescricao());
                                            }
                                        });
                                        variaveisGlobais.listConhecimentoPerfil = listaConhecimento.conhecimentosPerfil;

                                        listAdapter = new ListConhecimentosAdapter(getApplicationContext(), variaveisGlobais.listConhecimentoCurso);

                                        listViewConhecimentos.setAdapter(listAdapter);



                                    }

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_curriculo, menu);
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

    public class ConhecimentoList
    {
        public List<Conhecimento> conhecimentosCurso;
        public List<Conhecimento> conhecimentosPerfil;
    }



}
