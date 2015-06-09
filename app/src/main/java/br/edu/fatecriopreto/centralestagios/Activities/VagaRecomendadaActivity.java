package br.edu.fatecriopreto.centralestagios.Activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import br.edu.fatecriopreto.centralestagios.Entidades.Beneficio;
import br.edu.fatecriopreto.centralestagios.Entidades.Candidato;
import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.Entidades.Vaga;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Tabs.SlidingTabLayout;
import br.edu.fatecriopreto.centralestagios.Utils.ListVagasAdapter;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class VagaRecomendadaActivity extends ActionBarActivity {

    private Toolbar appBar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    //private ListView lsView;
    private ListVagasAdapter adapter;
    private ListView listVagaRecomendada;


    EditText edtFiltroNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaga_recomendada);

        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior()-1)) != VagaRecomendadaActivity.class)
            variaveisGlobais.setActivityAnterior(VagaRecomendadaActivity.class);
        variaveisGlobais.setAlert(VagaRecomendadaActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);
        String url = variaveisGlobais.EndIPAPP+"/vagas.aspx?rm=" + variaveisGlobais.getUserRm();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest getRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.getJSONArray(2).length() != 0){
                                for(int i = 0; i < response.getJSONArray(2).length(); i++) {
                                    Candidato candidato = new Candidato();
                                    candidato.setVagaId(response.getJSONArray(2).getJSONObject(i).getInt("VagaId"));
                                    variaveisGlobais.listCandidato.add(candidato);
                                }
                            }
                            else{
                                variaveisGlobais.listCandidato.clear();
                            }
                            filtrarVagasRecomendadas();
                            popularVagasRecomendadas();
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
        listVagaRecomendada = (ListView) findViewById(R.id.listVagaRecomendada);
        edtFiltroNome = (EditText) findViewById(R.id.edtFiltroNome);


        //Tabs
        /*
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        //Customizando as tabs
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);
        mTabs.setDistributeEvenly(true);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorLogo);
            }
        });
        mTabs.setViewPager(mPager);
        */

        /*final ArrayList<HashMap<String,String>> lista = new ArrayList<>();
        for(int i=0; i < 10; i++){
            HashMap<String,String> map = new HashMap<>();
            map.put(variaveisGlobais.KEY_ID,String.valueOf(i));
            map.put(variaveisGlobais.KEY_TITLE,"Nome vaga "+i);
            map.put(variaveisGlobais.KEY_COMPANY,"Nome empresa "+i);
            map.put(variaveisGlobais.KEY_SALARY,String.valueOf(i*20.0));
            lista.add(map);
        }

        lsView = (ListView) findViewById(R.id.listVagaRecomendada);

        adapter = new ListVagasAdapter(this, lista);
        lsView.setAdapter(adapter);

        lsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        */
        listVagaRecomendada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Vaga_ConsultarActivity.class);
                Bundle params = new Bundle();
                params.putString("titlevaga", variaveisGlobais.listVagasRecomendadas.get(position).getDescricao());
                params.putString("id", String.valueOf(variaveisGlobais.listVagasRecomendadas.get(position).getId()));
                params.putString("salary", String.valueOf(variaveisGlobais.listVagasRecomendadas.get(position).getRemuneracao()));
                params.putString("company", variaveisGlobais.listVagasRecomendadas.get(position).getEmpresa());
                params.putString("contact", variaveisGlobais.listVagasRecomendadas.get(position).getPessoaContato());
                params.putString("email", variaveisGlobais.listVagasRecomendadas.get(position).getEmailEmpresa());
                params.putString("hour", variaveisGlobais.listVagasRecomendadas.get(position).getHorario());
                if (variaveisGlobais.listVagasRecomendadas.get(position).getObservacoes() != null) {
                    params.putString("observation", variaveisGlobais.listVagasRecomendadas.get(position).getObservacoes());
                } else {
                    params.putString("observation", getResources().getString(R.string.semobs));
                }
                params.putString("type", variaveisGlobais.listVagasRecomendadas.get(position).getTipoVaga());
                params.putString("phone", variaveisGlobais.listVagasRecomendadas.get(position).getTelefoneEmpresa());
                params.putString("periody", variaveisGlobais.listVagasRecomendadas.get(position).getPeriodo());
                String beneficios = "";
                if (variaveisGlobais.listVagasRecomendadas.get(position).getBeneficio().isAuxilioOdontologico()) {
                    beneficios = getResources().getString(R.string.auxodont) + "\n";
                }
                if (variaveisGlobais.listVagasRecomendadas.get(position).getBeneficio().isPlanoSaude()) {
                    beneficios += getResources().getString(R.string.planosaude)+ "\n";
                }
                if (variaveisGlobais.listVagasRecomendadas.get(position).getBeneficio().isValeAlimentacao()) {
                    beneficios += getResources().getString(R.string.valeal)+"\n";
                }
                if (variaveisGlobais.listVagasRecomendadas.get(position).getBeneficio().isValeTransporte()) {
                    beneficios += "Vale Transporte \n";
                }
                if (variaveisGlobais.listVagasRecomendadas.get(position).getBeneficio().getOutros() != "null") {
                    beneficios += variaveisGlobais.listVagasRecomendadas.get(position).getBeneficio().getOutros();
                }
                params.putString("beneficts", beneficios);
                String conhecimentos = "";
                for (Conhecimento c : variaveisGlobais.listVagasRecomendadas.get(position).Conhecimentos) {
                    conhecimentos += c.getDescricao() + "\n";
                }
                params.putString("skills", conhecimentos);
                params.putString("Candidate", String.valueOf(variaveisGlobais.listVagasRecomendadas.get(position).isCandidatado()));
                //params.putString("position", String.valueOf(position));
                intent.putExtras(params);
                startActivity(intent);
                finish();
            }
        });



        //evento de text changed na edt de filtro de nome de vaga
            edtFiltroNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    final ArrayList<HashMap<String,String>> listaFiltrada = new ArrayList<>();
                    final ArrayList<Vaga> auxListaVagaRecomendada = new ArrayList<Vaga>();
                    filtrarVagasRecomendadas();
                    for(Vaga v: variaveisGlobais.listVagasRecomendadas){

                        if(v.getDescricao().toLowerCase().contains(edtFiltroNome.getText().toString().toLowerCase())){

                            HashMap<String,String> mapValue = new HashMap<>();
                            mapValue.put(variaveisGlobais.KEY_ID,String.valueOf(v.getId()));
                            mapValue.put(variaveisGlobais.KEY_TITLE, v.getDescricao());
                            mapValue.put(variaveisGlobais.KEY_COMPANY, v.getEmpresa());
                            mapValue.put(variaveisGlobais.KEY_SALARY,String.valueOf(v.getRemuneracao()));
                            for(Candidato c : variaveisGlobais.listCandidato){
                                if(v.getId() == c.getVagaId()) {
                                    mapValue.put(variaveisGlobais.KEY_CANDIDATE, "Candidatou-se");
                                    v.setCandidatado(true);
                                    break;
                                }else{
                                    mapValue.put(variaveisGlobais.KEY_CANDIDATE, "");
                                    v.setCandidatado(false);
                                }
                            }
                            auxListaVagaRecomendada.add(v);
                            listaFiltrada.add(mapValue);
                        }
                    }
                    variaveisGlobais.listVagasRecomendadas.clear();
                    variaveisGlobais.listVagasRecomendadas = auxListaVagaRecomendada;
                    adapter = new ListVagasAdapter(VagaRecomendadaActivity.this, listaFiltrada);
                    listVagaRecomendada.setAdapter(adapter);
                }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void popularVagasRecomendadas() {
        final ArrayList<HashMap<String,String>> lista = new ArrayList<>();
        for(Vaga v : variaveisGlobais.listVagasRecomendadas){
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
            lista.add(map);
        }

        adapter = new ListVagasAdapter(VagaRecomendadaActivity.this, lista);
        listVagaRecomendada.setAdapter(adapter);
    }
    public void filtrarVagasRecomendadas() {
        //Construir aqui o metodos que realiza o filtro em cima da lista de vagas globais
        try{
            variaveisGlobais.listVagasRecomendadas = new ArrayList<Vaga>();
            for (Vaga v : variaveisGlobais.listVagas){
                boolean verificaConhecimento = false;
                for (Conhecimento c : v.Conhecimentos){
                    for (Conhecimento cPerfil : variaveisGlobais.perfilRm.Conhecimentos){
                        if(c.getId() == cPerfil.getId()){
                            verificaConhecimento = true;
                            break;
                        }
                    }
                }
                if (verificaConhecimento){
                    variaveisGlobais.listVagasRecomendadas.add(v);
                }
            }
            Collections.sort(variaveisGlobais.listVagasRecomendadas, new Comparator<Vaga>() {
                @Override
                public int compare(Vaga lhs, Vaga rhs) {
                    return rhs.getDataCriacao().compareTo(lhs.getDataCriacao());
                }
            });
        }
        catch (Exception e){
            Log.d("ERRO", e.getMessage());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vaga_recomendada, menu);
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
    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] auxTabs;
        String tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            auxTabs = getResources().getStringArray(R.array.tabsvagas);
            tabs = auxTabs[2];
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.getInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }

    public static class  MyFragment extends Fragment{

        private TextView textView;

        public static MyFragment getInstance(int position){
            MyFragment myFragment =  new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
            View layout = null;
            Bundle bundle = getArguments();
            int position;
            if (bundle!=null){
                position = bundle.getInt("position");
                if(position==0){
                    layout = inflater.inflate(R.layout.activity_tabvaga, container, false);
                }
                else if(position==1){
                    layout= inflater.inflate(R.layout.activity_tabmensagem, container, false);
                }
            }
            return  layout;
        }
    }

    //Pega o evento de voltar do celular e volta para a activity anterior
    public void onBackPressed(){
        startActivity(new Intent(this, variaveisGlobais.getActivityAnterior(variaveisGlobais.getSizeActivityAnterior()-2)));
        variaveisGlobais.deleteAnterior();
        this.finish();
    }

}
