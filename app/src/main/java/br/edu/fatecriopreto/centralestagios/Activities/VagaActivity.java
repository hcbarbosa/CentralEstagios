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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Entidades.Beneficio;
import br.edu.fatecriopreto.centralestagios.Entidades.Candidato;
import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
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

        variaveisGlobais.setActivityAtual(this);

        final String urlNotificacao = variaveisGlobais.EndIPAPP + "/notificacoes.aspx?rm=" + variaveisGlobais.getUserRm() + "&acao=atualizar";

        RequestQueue queueNotificacao = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest getRequestNotificacao =
                new JsonObjectRequest(Request.Method.GET, urlNotificacao, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Error.Response", volleyError.getMessage());
                    }
                });

        queueNotificacao.add(getRequestNotificacao);

        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior()-1)) != VagaActivity.class)
            variaveisGlobais.setActivityAnterior(VagaActivity.class);
        variaveisGlobais.setAlert(VagaActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //DrawerFragment = menulateral
        final NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
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

                            ArrayList<Vaga> AuxiliarListaVagas = new ArrayList<Vaga>();
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
                                    AuxiliarListaVagas.add(vaga);
                                }
                            ArrayList<Vaga> AuxiliarListaVagasConhecimento = new ArrayList<Vaga>();
                                for(int i = 0; i < response.getJSONArray(3).length(); i++){
                                    for (int j = 0; j < response.getJSONArray(3).getJSONObject(i).getJSONArray("listaConhecimentos").length(); j++){
                                        Vaga vaga = new Vaga();

                                        vaga.setId(response.getJSONArray(3).getJSONObject(i).getInt("Id"));
                                        vaga.Conhecimentos = new ArrayList<Conhecimento>();
                                        Conhecimento conhecimento = new Conhecimento();
                                        conhecimento.setId(response.getJSONArray(3).getJSONObject(i).getJSONArray("listaConhecimentos").getJSONObject(j).getInt("Id"));
                                        conhecimento.setDescricao(response.getJSONArray(3).getJSONObject(i).getJSONArray("listaConhecimentos").getJSONObject(j).getString("Descricao"));
                                        conhecimento.setStatus(response.getJSONArray(3).getJSONObject(i).getJSONArray("listaConhecimentos").getJSONObject(j).getInt("Status"));
                                        conhecimento.setEstaSelecionado(response.getJSONArray(3).getJSONObject(i).getJSONArray("listaConhecimentos").getJSONObject(j).getBoolean("EstaSelecionado"));
                                        vaga.Conhecimentos.add(conhecimento);
                                        AuxiliarListaVagasConhecimento.add(vaga);
                                    }
                                }
                            if (AuxiliarListaVagas != null && !AuxiliarListaVagas.isEmpty()){
                                if (AuxiliarListaVagasConhecimento!= null && !AuxiliarListaVagasConhecimento.isEmpty()){
                                    for (Vaga v : AuxiliarListaVagas) {
                                        v.Conhecimentos = new ArrayList<Conhecimento>();
                                        for (Vaga vConhecimento : AuxiliarListaVagasConhecimento) {
                                            if (v.getId() == vConhecimento.getId()){
                                                    for (Conhecimento cConhecimento : vConhecimento.Conhecimentos){
                                                        if (cConhecimento != null) {
                                                            v.Conhecimentos.add(cConhecimento);
                                                        }
                                                    }
                                            }
                                        }
                                    }
                                    variaveisGlobais.listVagas = new ArrayList<Vaga>();
                                    variaveisGlobais.listVagas = AuxiliarListaVagas;
                                }
                                else{
                                    variaveisGlobais.listVagas = new ArrayList<Vaga>();
                                    variaveisGlobais.listVagas = AuxiliarListaVagas;
                                }

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
        ArrayList<Vaga> vaga = new ArrayList<Vaga>();
        final ArrayList<HashMap<String,String>> lista = new ArrayList<>();
        for(Vaga v : variaveisGlobais.listVagas){
            HashMap<String,String> map = new HashMap<>();
            map.put(variaveisGlobais.KEY_ID,String.valueOf(v.getId()));
            map.put(variaveisGlobais.KEY_TITLE,v.getDescricao());
            map.put(variaveisGlobais.KEY_COMPANY,v.getEmpresa());
            map.put(variaveisGlobais.KEY_SALARY,String.valueOf(v.getRemuneracao()));
            for(Candidato c : variaveisGlobais.listCandidato){
                if(v.getId() == c.getVagaId()) {
                    map.put(variaveisGlobais.KEY_CANDIDATE, "Candidatou-se");
                    v.setCandidatado(true);
                    break;
                }else{
                    map.put(variaveisGlobais.KEY_CANDIDATE, "");
                    v.setCandidatado(false);

                }
            }
            vaga.add(v);
            lista.add(map);
        }
        variaveisGlobais.listVagas = new ArrayList<Vaga>();
        variaveisGlobais.listVagas = vaga;

        listViewVagas = (ListView) findViewById(R.id.listViewVagas);

        adapter = new ListVagasAdapter(this, lista);
        listViewVagas.setAdapter(adapter);


        listViewVagas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Vaga_ConsultarActivity.class);
                Bundle params = new Bundle();
                params.putString("titlevaga", variaveisGlobais.listVagas.get(position).getDescricao());
                params.putString("id", String.valueOf(variaveisGlobais.listVagas.get(position).getId()));
                params.putString("salary", String.valueOf(variaveisGlobais.listVagas.get(position).getRemuneracao()));
                params.putString("company",variaveisGlobais.listVagas.get(position).getEmpresa());
                params.putString("contact", variaveisGlobais.listVagas.get(position).getPessoaContato());
                params.putString("email", variaveisGlobais.listVagas.get(position).getEmailEmpresa());
                params.putString("hour", variaveisGlobais.listVagas.get(position).getHorario());
                if (variaveisGlobais.listVagas.get(position).getObservacoes() != "null") {
                    params.putString("observation", variaveisGlobais.listVagas.get(position).getObservacoes());
                }
                else {
                    params.putString("observation", "Sem observação");
                }
                params.putString("type", variaveisGlobais.listVagas.get(position).getTipoVaga());
                params.putString("phone", variaveisGlobais.listVagas.get(position).getTelefoneEmpresa());
                params.putString("periody", variaveisGlobais.listVagas.get(position).getPeriodo());
                String beneficios = "";
                if (variaveisGlobais.listVagas.get(position).getBeneficio().isAuxilioOdontologico()) {
                    beneficios = "Auxilio Odontológico \n";
                }
                if (variaveisGlobais.listVagas.get(position).getBeneficio().isPlanoSaude()){
                    beneficios  += "Plano de Saúde \n";
                }
                if (variaveisGlobais.listVagas.get(position).getBeneficio().isValeAlimentacao()){
                    beneficios  += "Vale Alimentação \n";
                }
                if (variaveisGlobais.listVagas.get(position).getBeneficio().isValeTransporte()){
                    beneficios  += "Vale Transporte \n";
                }
                if (variaveisGlobais.listVagas.get(position).getBeneficio().getOutros() != "null"){
                    beneficios  += variaveisGlobais.listVagas.get(position).getBeneficio().getOutros();
                }
                params.putString("beneficts", beneficios);
                String conhecimentos = "";
                for (Conhecimento c : variaveisGlobais.listVagas.get(position).Conhecimentos) {
                    conhecimentos += c.getDescricao() + "\n";
                }
                params.putString("skills",conhecimentos );
                params.putString("position", String.valueOf(position));
                intent.putExtras(params);
                startActivity(intent);
                finish();
            }
        });
    }
    public void filtrarVagasRecomendadas() {
        //Construir aqui o metodos que realiza o filtro em cima da lista de vagas globais
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
