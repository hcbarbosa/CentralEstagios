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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Tabs.SlidingTabLayout;
import br.edu.fatecriopreto.centralestagios.Utils.ListVagasAdapter;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class VagaPesquisaAvancadaActivity extends ActionBarActivity {

    private Toolbar appBar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private ListView lsView;
    private ListVagasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaga_pesquisa_avancada);

        variaveisGlobais.setActivityAtual(this);

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

        final ArrayList<HashMap<String,String>> lista = new ArrayList<>();
        for(int i=0; i < 10; i++){
            HashMap<String,String> map = new HashMap<>();
            map.put(variaveisGlobais.KEY_ID,String.valueOf(i));
            map.put(variaveisGlobais.KEY_TITLE,"Nome vaga "+i);
            map.put(variaveisGlobais.KEY_COMPANY,"Empresa: "+"Nome empresa "+i);
            map.put(variaveisGlobais.KEY_SALARY, getResources().getString(R.string.salario)+": "+String.valueOf(i*20.0));
            //verifica se candidatou-se, tem q pegar da tabela candidatos se 1 = true
            if(i%2 == 0) {
                map.put(variaveisGlobais.KEY_CANDIDATE, "Candidatou-se");
            }else{
                map.put(variaveisGlobais.KEY_CANDIDATE, "");
            }
            lista.add(map);
        }

        lsView = (ListView) findViewById(R.id.listVagaAvancada);

        adapter = new ListVagasAdapter(this, lista);
        lsView.setAdapter(adapter);

        lsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
