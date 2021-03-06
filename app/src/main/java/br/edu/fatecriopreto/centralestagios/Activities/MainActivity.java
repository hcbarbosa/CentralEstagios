package br.edu.fatecriopreto.centralestagios.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Entidades.Beneficio;
import br.edu.fatecriopreto.centralestagios.Entidades.Candidato;
import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.Entidades.Perfil;
import br.edu.fatecriopreto.centralestagios.Entidades.Vaga;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Tabs.SlidingTabLayout;
import br.edu.fatecriopreto.centralestagios.Utils.ListMensagemAdapter;
import br.edu.fatecriopreto.centralestagios.Utils.ListVagasAdapter;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;
import br.edu.fatecriopreto.centralestagios.notificacao;

public class MainActivity extends ActionBarActivity {

    private Toolbar appBar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Auxiliar na transicao de telas e pilhas
        if(variaveisGlobais.getSizeActivityAnterior()!=0 && variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior()-1)) != MainActivity.class)
            variaveisGlobais.setActivityAnterior(MainActivity.class);
        else{
            variaveisGlobais.setActivityAnterior(MainActivity.class);
        }
        variaveisGlobais.setAlert(MainActivity.this);

        notificacao notificacao = new notificacao();
        notificacao.comecarOuvidorNotificacoes();

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //coloca qual a img do usuario
        variaveisGlobais.setImageUser(R.drawable.app_icon);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
        getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);


        //Tabs
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

        }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate themenu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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

        /*if(id == R.id.search)
        {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    class MyPagerAdapter extends FragmentPagerAdapter{

        String[] tabs;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.getInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public static class  MyFragment extends Fragment{

        private ListView listViewVagas;
        private ListVagasAdapter adapter;
        private EditText edtFiltroNome;
        public ProgressBar mProgressBar;
        ArrayList<Vaga> auxCloneListVagas;


        private ListView listViewMensagens;
        private ListMensagemAdapter listAdapter;
        List<Vaga> listaRooms = new ArrayList<>();


        public static Date convertDate(String date, String format)
                throws ParseException {
            if(date != null) {
                SimpleDateFormat sdf = format != null ? new SimpleDateFormat(format) : new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                return sdf.parse(date);
            } else {
                return null;
            }
        }
        public void atualizarConhecimentoPerfil(){
            String url2 = variaveisGlobais.EndIPAPP+"/perfil.aspx?rm="+variaveisGlobais.getUserRm()+"&acao=obter";
            RequestQueue fila = Volley.newRequestQueue(getActivity().getApplicationContext());

            JsonArrayRequest getRequesicao = new JsonArrayRequest(url2,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray resposta) {

                            try {
                                variaveisGlobais.perfilRm = new Perfil();
                                variaveisGlobais.perfilRm.Conhecimentos = new ArrayList<Conhecimento>();
                                variaveisGlobais.perfilRm.setRm(resposta.getJSONObject(0).getInt("LoginRm"));
                                variaveisGlobais.perfilRm.setCursoId(resposta.getJSONObject(0).getInt("CursoId"));
                                variaveisGlobais.perfilRm.setTelefone(resposta.getJSONObject(0).getString("Telefone"));
                                variaveisGlobais.perfilRm.setCEP(resposta.getJSONObject(0).getString("Cep"));
                                variaveisGlobais.perfilRm.setUf(resposta.getJSONObject(0).getString("Uf"));
                                variaveisGlobais.perfilRm.setCidade(resposta.getJSONObject(0).getString("Cidade"));
                                variaveisGlobais.perfilRm.setBairro(resposta.getJSONObject(0).getString("Bairro"));
                                variaveisGlobais.perfilRm.setLogradouro(resposta.getJSONObject(0).getString("Logradouro"));
                                variaveisGlobais.perfilRm.setComplemento(resposta.getJSONObject(0).getString("Complemento"));
                                variaveisGlobais.perfilRm.setAno(resposta.getJSONObject(0).getLong("Ano"));
                                variaveisGlobais.perfilRm.setSemestre(resposta.getJSONObject(0).getString("Semestre"));
                                variaveisGlobais.perfilRm.setNome(resposta.getJSONObject(0).getString("Nome"));
                                variaveisGlobais.perfilRm.setEmail(resposta.getJSONObject(0).getString("Email"));
                                for(int i = 0; i < resposta.getJSONArray(1).length(); i++) {
                                    Conhecimento conhecimento = new Conhecimento();
                                    conhecimento.setId(resposta.getJSONArray(1).getJSONObject(i).getInt("Id"));
                                    conhecimento.setDescricao(resposta.getJSONArray(1).getJSONObject(i).getString("Descricao"));
                                    conhecimento.setStatus(resposta.getJSONArray(1).getJSONObject(i).getInt("Status"));
                                    conhecimento.setEstaSelecionado(resposta.getJSONArray(1).getJSONObject(i).getBoolean("EstaSelecionado"));
                                    variaveisGlobais.perfilRm.Conhecimentos.add(conhecimento);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response.Aki", "Erro no webservice");
                }
            });

            fila.add(getRequesicao);
        }
        public void popularVagas() {
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
                lista.add(map);
            }

            adapter = new ListVagasAdapter(getActivity(), lista);
            listViewVagas.setAdapter(adapter);
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            listViewVagas.setVisibility(View.VISIBLE);
        }

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
            //posicao da tab
            if (bundle!=null){
                position = bundle.getInt("position");
                if(position==0){
                    layout = inflater.inflate(R.layout.activity_tabvaga, container, false);
                    listViewVagas = (ListView) layout.findViewById(R.id.listViewVagasTab);
                    edtFiltroNome = (EditText) layout.findViewById(R.id.edtFiltroNomeTab);
                    edtFiltroNome.setHint("Digite uma vaga");
                    listViewVagas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(view.getContext(), Vaga_ConsultarActivity.class);
                            Bundle params = new Bundle();
                            params.putString("titlevaga", auxCloneListVagas.get(position).getDescricao());
                            params.putString("id", String.valueOf(auxCloneListVagas.get(position).getId()));
                            params.putString("salary", String.valueOf(auxCloneListVagas.get(position).getRemuneracao()));
                            params.putString("company", auxCloneListVagas.get(position).getEmpresa());
                            params.putString("contact", auxCloneListVagas.get(position).getPessoaContato());
                            params.putString("email", auxCloneListVagas.get(position).getEmailEmpresa());
                            params.putString("hour", auxCloneListVagas.get(position).getHorario());
                            if (auxCloneListVagas.get(position).getObservacoes() != null) {
                                params.putString("observation", auxCloneListVagas.get(position).getObservacoes());
                            } else {
                                params.putString("observation", getResources().getString(R.string.semobs));
                            }
                            params.putString("type", auxCloneListVagas.get(position).getTipoVaga());
                            params.putString("phone", auxCloneListVagas.get(position).getTelefoneEmpresa());
                            params.putString("periody", auxCloneListVagas.get(position).getPeriodo());
                            String beneficios = "";
                            if (auxCloneListVagas.get(position).getBeneficio().isAuxilioOdontologico()) {
                                beneficios = getResources().getString(R.string.auxodont) + "\n";
                            }
                            if (auxCloneListVagas.get(position).getBeneficio().isPlanoSaude()) {
                                beneficios += getResources().getString(R.string.planosaude) + "\n";
                            }
                            if (auxCloneListVagas.get(position).getBeneficio().isValeAlimentacao()) {
                                beneficios += getResources().getString(R.string.valeal) + "\n";
                            }
                            if (auxCloneListVagas.get(position).getBeneficio().isValeTransporte()) {
                                beneficios += "Vale Transporte \n";
                            }
                            if (auxCloneListVagas.get(position).getBeneficio().getOutros() != "null") {
                                beneficios += auxCloneListVagas.get(position).getBeneficio().getOutros();
                            }
                            params.putString("beneficts", beneficios);
                            String conhecimentos = "";
                            for (Conhecimento c : auxCloneListVagas.get(position).Conhecimentos) {
                                conhecimentos += c.getDescricao() + "\n";
                            }
                            params.putString("skills", conhecimentos);
                            params.putString("Candidate", String.valueOf(auxCloneListVagas.get(position).isCandidatado()));
                            //params.putString("position", String.valueOf(position));
                            intent.putExtras(params);
                            startActivity(intent);
                            //finish();
                        }
                    });
                    edtFiltroNome.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }



                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            final ArrayList<HashMap<String, String>> listaFiltrada = new ArrayList<>();
                            auxCloneListVagas = new ArrayList<Vaga>();
                            for (Vaga x : variaveisGlobais.listVagas) {
                                auxCloneListVagas.add(x);
                            }
                            final ArrayList<Vaga> auxListaVaga = new ArrayList<Vaga>();
                            for (Vaga v : auxCloneListVagas) {

                                if (v.getDescricao().toLowerCase().contains(edtFiltroNome.getText().toString().toLowerCase())) {

                                    HashMap<String, String> mapValue = new HashMap<>();
                                    mapValue.put(variaveisGlobais.KEY_ID, String.valueOf(v.getId()));
                                    mapValue.put(variaveisGlobais.KEY_TITLE, v.getDescricao());
                                    mapValue.put(variaveisGlobais.KEY_COMPANY, v.getEmpresa());
                                    mapValue.put(variaveisGlobais.KEY_SALARY, String.valueOf(v.getRemuneracao()));
                                    for (Candidato c : variaveisGlobais.listCandidato) {
                                        if (v.getId() == c.getVagaId()) {
                                            mapValue.put(variaveisGlobais.KEY_CANDIDATE, "Candidatou-se");
                                            v.setCandidatado(true);
                                            break;
                                        } else {
                                            mapValue.put(variaveisGlobais.KEY_CANDIDATE, "");
                                            v.setCandidatado(false);
                                        }
                                    }
                                    auxListaVaga.add(v);
                                    listaFiltrada.add(mapValue);
                                }
                            }
                            auxCloneListVagas.clear();
                            for (Vaga x : auxListaVaga) {
                                auxCloneListVagas.add(x);
                            }
                            adapter = new ListVagasAdapter(getActivity(), listaFiltrada);
                            listViewVagas.setAdapter(adapter);

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    String url = variaveisGlobais.EndIPAPP+"/vagas.aspx?rm=" + variaveisGlobais.getUserRm();
                    RequestQueue queueVagas = Volley.newRequestQueue(getActivity().getApplicationContext());
                    //RequestQueue queue = Volley.newRequestQueue(layout.getContext());

                    JsonArrayRequest getRequest = new JsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {

                                @Override
                                public void onResponse(JSONArray response) {
                                    try {

                                        ArrayList<Vaga> AuxiliarListaVagas = new ArrayList<>();
                                        variaveisGlobais.listCandidato = new ArrayList<>();
                                        for (int i = 0; i < response.getJSONArray(0).length(); i++) {
                                            Vaga vaga = new Vaga();
                                            //achar um jeito de definir qual array dentro do response sera utilizado para verificar um getJSONObject
                                            vaga.setId(response.getJSONArray(0).getJSONObject(i).getInt("Id"));
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

                                            vaga.setDataCriacao(convertDate(response.getJSONArray(0).getJSONObject(i).getString("DataString"),"dd/MM/yyyy"));
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
                                        ArrayList<Vaga> AuxiliarListaVagasConhecimento = new ArrayList<>();
                                        for(int i = 0; i < response.getJSONArray(3).length(); i++){
                                            for (int j = 0; j < response.getJSONArray(3).getJSONObject(i).getJSONArray("listaConhecimentos").length(); j++){
                                                Vaga vaga = new Vaga();

                                                vaga.setId(response.getJSONArray(3).getJSONObject(i).getInt("Id"));
                                                vaga.Conhecimentos = new ArrayList<>();
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
                                                    v.Conhecimentos = new ArrayList<>();
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
                                                variaveisGlobais.listVagas = new ArrayList<>();
                                                variaveisGlobais.listVagas = AuxiliarListaVagas;
                                            }
                                            else{
                                                variaveisGlobais.listVagas = new ArrayList<>();
                                                variaveisGlobais.listVagas = AuxiliarListaVagas;
                                            }


                                        }
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
                                        if(variaveisGlobais.listVagas != null && !variaveisGlobais.listVagas.isEmpty()) {
                                            Collections.sort(variaveisGlobais.listVagas, new Comparator<Vaga>() {
                                                @Override
                                                public int compare(Vaga lhs, Vaga rhs) {
                                                    return rhs.getDataCriacao().compareTo(lhs.getDataCriacao());
                                                }
                                            });
                                            auxCloneListVagas = new ArrayList<Vaga>();
                                            for (Vaga v : variaveisGlobais.listVagas){
                                                auxCloneListVagas.add(v);
                                            }
                                            atualizarConhecimentoPerfil();
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

                    queueVagas.add(getRequest);

                }
                else if(position==1){
                    layout= inflater.inflate(R.layout.activity_tabmensagem, container, false);

                    listViewMensagens = (ListView) layout.findViewById(R.id.roomsListTab);
                    listViewMensagens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(view.getContext(), ChatActivity.class);
                            Bundle params = new Bundle();
                            params.putString("titlevaga", listaRooms.get(position).getDescricao());
                            params.putString("id", String.valueOf(listaRooms.get(position).getId()));
                            intent.putExtras(params);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });

                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

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

                                    final RequestQueue queueExcluir = Volley.newRequestQueue(getActivity().getApplicationContext());

                                    final JsonObjectRequest getRequest =
                                            new JsonObjectRequest(Request.Method.GET, url, null,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject jsonObject) {

                                                            try {
                                                                if (jsonObject.getString("Conteudo").equals("ok")) {

                                                                    Toast.makeText(getActivity(), "Chat excluido com sucesso!", Toast.LENGTH_LONG).show();
                                                                    listViewMensagens.removeViewInLayout(v);
                                                                    variaveisGlobais.listRooms.remove(pos);
                                                                    listAdapter.notifyDataSetChanged();
                                                                } else {
                                                                    Toast.makeText(getActivity(), "Erro ao excluir chat, tente mais tarde!", Toast.LENGTH_LONG).show();
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

                    RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

                    JsonArrayRequest getRequest =
                            new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray jsonArray) {

                                    if(jsonArray != null) {
                                        Gson gson = new Gson();

                                        MensagemActivity.AuxListaVaga auxiliar = null;
                                        try {
                                            auxiliar = gson.fromJson(jsonArray.get(0).toString(), MensagemActivity.AuxListaVaga.class);
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

                                        listAdapter = new ListMensagemAdapter(variaveisGlobais.listRooms, getActivity());
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
            }


            return  layout;
        }

    }


    //Pega o evento de voltar do celular e volta para a activity anterior
    public void onBackPressed(){
        startActivity(new Intent(this, LoginActivity.class));
        variaveisGlobais.deleteAnterior();
        this.finish();
    }

}
