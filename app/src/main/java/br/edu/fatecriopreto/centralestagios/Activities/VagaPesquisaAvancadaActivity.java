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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Entidades.Beneficio;
import br.edu.fatecriopreto.centralestagios.Entidades.Candidato;
import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.Entidades.Vaga;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Tabs.SlidingTabLayout;
import br.edu.fatecriopreto.centralestagios.Utils.ListVagasAdapter;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class VagaPesquisaAvancadaActivity extends ActionBarActivity {

    private Toolbar appBar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private ListView lsViewAvancada;
    private ListVagasAdapter adapter;
    EditText edtFiltroNome;
    EditText edtFiltro;
    RadioButton rdbBeneficio;
    RadioButton rdbConhecimento;
    RadioGroup rdbGroup;
    private ArrayList<Vaga> auxListVagaAvancada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaga_pesquisa_avancada);

        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior()-1)) != VagaPesquisaAvancadaActivity.class)
            variaveisGlobais.setActivityAnterior(VagaPesquisaAvancadaActivity.class);
        variaveisGlobais.setAlert(VagaPesquisaAvancadaActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);

        edtFiltroNome = (EditText) findViewById(R.id.edtFiltroNome);
        edtFiltro = (EditText) findViewById(R.id.edtFiltro);
        rdbGroup = (RadioGroup) findViewById(R.id.rdbGroup);
        rdbBeneficio = (RadioButton) findViewById(R.id.rdbBeneficio);
        rdbConhecimento= (RadioButton) findViewById(R.id.rdbConhecimento);
        lsViewAvancada = (ListView) findViewById(R.id.listVagaAvancada);
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
                            popularVagas();
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
        lsViewAvancada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), Vaga_ConsultarActivity.class);
                Bundle params = new Bundle();
                params.putString("titlevaga", auxListVagaAvancada.get(position).getDescricao());
                params.putString("id", String.valueOf(auxListVagaAvancada.get(position).getId()));
                params.putString("salary", String.valueOf(auxListVagaAvancada.get(position).getRemuneracao()));
                params.putString("company", auxListVagaAvancada.get(position).getEmpresa());
                params.putString("contact", auxListVagaAvancada.get(position).getPessoaContato());
                params.putString("email", auxListVagaAvancada.get(position).getEmailEmpresa());
                params.putString("hour", auxListVagaAvancada.get(position).getHorario());
                if (auxListVagaAvancada.get(position).getObservacoes() != null) {
                    params.putString("observation", auxListVagaAvancada.get(position).getObservacoes());
                } else {
                    params.putString("observation", getResources().getString(R.string.semobs));
                }
                params.putString("type", auxListVagaAvancada.get(position).getTipoVaga());
                params.putString("phone", auxListVagaAvancada.get(position).getTelefoneEmpresa());
                params.putString("periody", auxListVagaAvancada.get(position).getPeriodo());
                String beneficios = "";
                if (auxListVagaAvancada.get(position).getBeneficio().isAuxilioOdontologico()) {
                    beneficios = getResources().getString(R.string.auxodont) + "\n";
                }
                if (auxListVagaAvancada.get(position).getBeneficio().isPlanoSaude()) {
                    beneficios += getResources().getString(R.string.planosaude)+ "\n";
                }
                if (auxListVagaAvancada.get(position).getBeneficio().isValeAlimentacao()) {
                    beneficios += getResources().getString(R.string.valeal)+"\n";
                }
                if (auxListVagaAvancada.get(position).getBeneficio().isValeTransporte()) {
                    beneficios += "Vale Transporte \n";
                }
                if (auxListVagaAvancada.get(position).getBeneficio().getOutros() != "null") {
                    beneficios += auxListVagaAvancada.get(position).getBeneficio().getOutros();
                }
                params.putString("beneficts", beneficios);
                String conhecimentos = "";
                for (Conhecimento c : auxListVagaAvancada.get(position).Conhecimentos) {
                    conhecimentos += c.getDescricao() + "\n";
                }
                params.putString("skills", conhecimentos);
                params.putString("Candidate", String.valueOf(auxListVagaAvancada.get(position).isCandidatado()));
                //params.putString("position", String.valueOf(position));
                intent.putExtras(params);
                startActivity(intent);
                finish();
            }
        });

        rdbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdbBeneficio.isChecked()){
                    edtFiltro.setHint("Digite um beneficio");
                    edtFiltro.setEnabled(true);
                    popularVagas();
                }
                else if (rdbConhecimento.isChecked()){
                    edtFiltro.setHint("Digite um conhecimento");
                    edtFiltro.setEnabled(true);
                    popularVagas();
                }


            }
        });

        edtFiltro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                auxListVagaAvancada = new ArrayList<Vaga>();
                for (Vaga v : variaveisGlobais.listVagas){
                    auxListVagaAvancada.add(v);
                }
                    if (rdbBeneficio.isChecked()){
                        final ArrayList<HashMap<String,String>> listaFiltrada = new ArrayList<>();
                        final ArrayList<Vaga> auxListaVagaFiltro = new ArrayList<Vaga>();

                        for(Vaga v: auxListVagaAvancada){
                            String beneficio = "";
                            if(v.getBeneficio().isAuxilioOdontologico()) {
                                beneficio += "auxilio odontologico ";
                            }
                            if (v.getBeneficio().isPlanoSaude()){
                                beneficio+="plano de saude ";
                            }
                            if (v.getBeneficio().isValeAlimentacao()){
                                beneficio+="vale alimentacao ";
                            }
                            if (v.getBeneficio().isValeTransporte()){
                                beneficio+="vale transporte ";
                            }
                            if (v.getBeneficio().getOutros() != "null"){
                                beneficio+=v.getBeneficio().getOutros();
                            }
                            if (beneficio.contains(edtFiltro.getText().toString().toLowerCase())){
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
                                auxListaVagaFiltro.add(v);
                                listaFiltrada.add(mapValue);
                            }
                        }
                        auxListVagaAvancada = auxListaVagaFiltro;
                        Collections.sort(auxListVagaAvancada, new Comparator<Vaga>() {
                            @Override
                            public int compare(Vaga lhs, Vaga rhs) {
                                return rhs.getDataCriacao().compareTo(lhs.getDataCriacao());
                            }
                        });
                        adapter = new ListVagasAdapter(VagaPesquisaAvancadaActivity.this, listaFiltrada);
                        lsViewAvancada.setAdapter(adapter);
                    }
                    else if (rdbConhecimento.isChecked()){
                        final ArrayList<HashMap<String,String>> listaFiltrada = new ArrayList<>();
                        final ArrayList<Vaga> auxListaVagaFiltro = new ArrayList<Vaga>();
                        for(Vaga v: auxListVagaAvancada){
                            for (Conhecimento vConhecimento : v.Conhecimentos){
                                if (vConhecimento.getDescricao().toLowerCase().contains(edtFiltro.getText().toString().toLowerCase())){
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
                                    auxListaVagaFiltro.add(v);
                                    listaFiltrada.add(mapValue);
                                    break;
                                   }
                                }
                            }
                        auxListVagaAvancada = auxListaVagaFiltro;
                        Collections.sort(auxListVagaAvancada, new Comparator<Vaga>() {
                            @Override
                            public int compare(Vaga lhs, Vaga rhs) {
                                return rhs.getDataCriacao().compareTo(lhs.getDataCriacao());
                            }
                        });
                        adapter = new ListVagasAdapter(VagaPesquisaAvancadaActivity.this, listaFiltrada);
                        lsViewAvancada.setAdapter(adapter);
                    }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }
    public void popularVagas() {
        final ArrayList<HashMap<String,String>> lista = new ArrayList<>();
        Collections.sort(variaveisGlobais.listVagas, new Comparator<Vaga>() {
            @Override
            public int compare(Vaga lhs, Vaga rhs) {
                return rhs.getDataCriacao().compareTo(lhs.getDataCriacao());
            }
        });
        auxListVagaAvancada = new ArrayList<Vaga>();
        for(Vaga v : variaveisGlobais.listVagas){
            auxListVagaAvancada.add(v);
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

        adapter = new ListVagasAdapter(this, lista);
        lsViewAvancada.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vaga_pesquisa_avancada, menu);
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
            tabs = auxTabs[1];
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
