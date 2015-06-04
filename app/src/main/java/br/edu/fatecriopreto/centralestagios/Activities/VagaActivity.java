package br.edu.fatecriopreto.centralestagios.Activities;

import android.content.Intent;
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
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.edu.fatecriopreto.centralestagios.Entidades.Beneficio;
import br.edu.fatecriopreto.centralestagios.Entidades.Candidato;
import br.edu.fatecriopreto.centralestagios.Entidades.Vaga;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Utils.ListVagasAdapter;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;


public class VagaActivity extends ActionBarActivity {


    private Toolbar appBar;
    private Button btnCandidatar;
    private ListView listViewVagas;
    private ListVagasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaga);

        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior()-1)) != VagaActivity.class)
            variaveisGlobais.setActivityAnterior(VagaActivity.class);
        variaveisGlobais.setAlert(VagaActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);


        /*btnCandidatar = (Button) findViewById(R.id.btnCandidatar);
        btnCandidatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candidatar();
            }
        });
        */
        String url = variaveisGlobais.EndIPAPP+"/vagas.aspx?rm=" + variaveisGlobais.getUserRm();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest getRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            variaveisGlobais.listVagas = new ArrayList<Vaga>();
                            variaveisGlobais.listCandidato = new ArrayList<Candidato>();
                                for (int i = 0; i < response.getJSONArray(0).length(); i++) {
                                    Vaga vaga = new Vaga();
                                    //achar um jeito de definir qual array dentro do response sera utilizado para verificar um getJSONObject
                                    vaga.setId(response.getJSONArray(0).getJSONObject(i).getInt("Id"));
                                    //vaga.setBeneficio(Integer.parseInt(response.getJSONObject(1).toString();
                                    vaga.setBeneficioId(response.getJSONArray(0).getJSONObject(i).getInt("BeneficioId"));
                                    vaga.setDescricao(response.getJSONArray(0).getJSONObject(i).getString("Descricao"));
                                    vaga.setTelefoneEmpresa(response.getJSONArray(0).getJSONObject(i).getString("TelefoneEmpresa"));
                                    vaga.setHorario(response.getJSONArray(0).getJSONObject(i).getString("Horario"));
                                    vaga.setPessoaContato(response.getJSONArray(0).getJSONObject(i).getString("PessoaContato"));
                                    vaga.setPeriodo(response.getJSONArray(0).getJSONObject(i).getString("Periodo"));
                                    vaga.setTipoVaga(response.getJSONArray(0).getJSONObject(i).getString("TipoVaga"));
                                    vaga.setEmpresa(response.getJSONArray(0).getJSONObject(i).getString("Empresa"));
                                    vaga.setRemuneracao(response.getJSONArray(0).getJSONObject(i).getDouble("Remuneracao"));
                                    vaga.setEmailEmpresa(response.getJSONArray(0).getJSONObject(i).getString("EmailEmpresa"));
                                    vaga.setObservacoes(response.getJSONArray(0).getJSONObject(i).getString("Observacoes"));
                                    //vaga.setDataCriacao(response.getJSONObject(13).toString());
                                    for(int j = 0; j < response.getJSONArray(1).length(); j++) {
                                        if(vaga.getBeneficioId() == response.getJSONArray(1).getJSONObject(j).getInt("Id")) {
                                            Beneficio beneficio = new Beneficio();
                                            beneficio.setId(response.getJSONArray(1).getJSONObject(j).getInt("Id"));
                                            beneficio.setAuxilioOdontologico(response.getJSONArray(1).getJSONObject(j).getBoolean("AuxilioOdontologico"));
                                            beneficio.setPlanoSaude(response.getJSONArray(1).getJSONObject(j).getBoolean("PlanoSaude"));
                                            beneficio.setValeAlimentacao(response.getJSONArray(1).getJSONObject(j).getBoolean("ValeAlimentacao"));
                                            beneficio.setValeTransporte(response.getJSONArray(1).getJSONObject(j).getBoolean("ValeTransporte"));
                                            beneficio.setOutros(response.getJSONArray(1).getJSONObject(j).getString("Outros"));
                                            vaga.setBeneficio(beneficio);
                                        }
                                    }
                                    variaveisGlobais.listVagas.add(vaga);
                                }
                            for(int i = 0; i < response.getJSONArray(2).length(); i++) {
                                Candidato candidato = new Candidato();
                                candidato.setVagaId(response.getJSONArray(2).getJSONObject(i).getInt("VagaId"));
                                variaveisGlobais.listCandidato.add(candidato);
                            }
                            if(variaveisGlobais.listVagas != null && !variaveisGlobais.listVagas.isEmpty()) {

                                popularVagas();
                            }

                        } catch (Exception e) {
                            Log.d("erro: ", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        queue.add(getRequest);
    }
    public void popularVagas() {
        final ArrayList<HashMap<String,String>> lista = new ArrayList<>();
        for(Vaga v : variaveisGlobais.listVagas){
            HashMap<String,String> map = new HashMap<>();
            map.put(variaveisGlobais.KEY_ID,String.valueOf(v.getId()));
            map.put(variaveisGlobais.KEY_TITLE,v.getDescricao());
            map.put(variaveisGlobais.KEY_COMPANY,v.getEmpresa());
            map.put(variaveisGlobais.KEY_SALARY,String.valueOf(v.getRemuneracao()));
            //verifica se candidatou-se, tem q pegar da tabela candidatos se 1 = true
            for(Candidato c : variaveisGlobais.listCandidato){
                if(v.getId() == c.getVagaId()) {
                    map.put(variaveisGlobais.KEY_CANDIDATE, "Candidatou-se");
                    break;
                }else{
                    map.put(variaveisGlobais.KEY_CANDIDATE, "");
                }
            }
            lista.add(map);
        }

        listViewVagas = (ListView) findViewById(R.id.listViewVagas);

        adapter = new ListVagasAdapter(this, lista);
        listViewVagas.setAdapter(adapter);


        listViewVagas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Vaga_ConsultarActivity.class);
                Bundle params = new Bundle();
                params.putString("titlevaga", lista.get(position).get("titlevaga"));
                params.putString("id", lista.get(position).get("id"));
                params.putString("salary", lista.get(position).get("salary"));
                params.putString("company", lista.get(position).get("company"));

                intent.putExtras(params);
                startActivity(intent);
                finish();
            }
        });
    }
    public void candidatar(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        //editar o email
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"emailcompany@email.com"});
        emailIntent.putExtra(Intent.EXTRA_CC, new String[]{"emailcentralestagios@email.com"});
        //emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{"emailcentralestagios@email.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Vaga number - nome da vaga");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Segue abaixo o curriculo do candidato: nome do candidato");

        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent,"Escolha seu aplicativo de email..."));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vaga, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.pesqsimples) {
            startActivity(new Intent(this, VagaActivity.class));
            this.finish();
        }else if (id == R.id.pesqavancada) {
            startActivity(new Intent(this, VagaPesquisaAvancadaActivity.class));
            this.finish();
        }else if (id == R.id.pesqrecomendada) {
            startActivity(new Intent(this, VagaRecomendadaActivity.class));
            this.finish();
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
