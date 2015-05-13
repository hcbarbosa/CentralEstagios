package br.edu.fatecriopreto.centralestagios.Menu;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Activities.ConfiguracoesActivity;
import br.edu.fatecriopreto.centralestagios.Activities.CurriculoActivity;
import br.edu.fatecriopreto.centralestagios.Activities.LoginActivity;
import br.edu.fatecriopreto.centralestagios.Activities.MainActivity;
import br.edu.fatecriopreto.centralestagios.Activities.MensagemActivity;
import br.edu.fatecriopreto.centralestagios.Activities.PerfilActivity;
import br.edu.fatecriopreto.centralestagios.Activities.VagaActivity;
import br.edu.fatecriopreto.centralestagios.Entidades.Perfil;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements InformationAdapter.ClickListener {

    private RecyclerView recyclerView;
    public static final  String PREF_FILE_NAME = "testPref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private InformationAdapter adapter;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
        mFromSavedInstanceState = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if(savedInstanceState != null){
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        //cria o menu
        adapter = new InformationAdapter(getActivity(),getListaMenu());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public List<Information> getListaMenu(){
        List<Information> listMenu = new ArrayList<>();
        int[] icons = {R.drawable.ic_home,R.drawable.ic_account,
                R.drawable.ic_curriculo,R.drawable.ic_vagas,
                R.drawable.ic_message,R.drawable.ic_settings,
                R.drawable.ic_exit_to_app};
        String[] titles = new String[]{getResources().getString(R.string.inicio),
                getResources().getString(R.string.perfil),getResources().getString(R.string.curriculo),
                getResources().getString(R.string.vagas),getResources().getString(R.string.mensagens),
                getResources().getString(R.string.configuracoes),getResources().getString(R.string.sair)};

        for(int i = 0; i < icons.length && i < titles.length; i++)
        {
            Information info = new Information();
            info.iconid = icons[i];
            info.title = titles[i];
            listMenu.add(info);
        }

        return listMenu;
    }


    public void setUp(int fragment_navigation_drawer, DrawerLayout drawerLayout, final Toolbar appBar) {
        containerView = getActivity().findViewById(fragment_navigation_drawer);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, appBar,
                R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer)
                {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
                appBar.setTitle(getResources().getString(R.string.menu));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                appBar.setTitle(getActivity().getTitle());
            }


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView,slideOffset);
                PerfilActivity.onDrawerSlide(slideOffset);
                if(slideOffset < 0.6) {
                    appBar.setAlpha(2 - slideOffset);
                }
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
                if(!mUserLearnedDrawer && !mFromSavedInstanceState){
                    mDrawerLayout.openDrawer(containerView);

                }
            }
        });
    }

    public void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(preferenceName,preferenceValue);
    }

    @Override
    public void itemClicked(View view, int position) {
        switch (position){
            //aqui que vai muda o layout do main
             case 1: //"Inicio"
                startActivity(new Intent(getActivity(), MainActivity.class));
                 variaveisGlobais.setActivityAnterior(variaveisGlobais.getActivityAtual());
                 getActivity().finish();
                 break;
             case 2: //"Perfil"
                 startActivity(new Intent(getActivity(), PerfilActivity.class));
                 variaveisGlobais.setActivityAnterior(variaveisGlobais.getActivityAtual());
                 getActivity().finish();
                break;
            case 3: //"Curriculo"
                startActivity(new Intent(getActivity(), CurriculoActivity.class));
                variaveisGlobais.setActivityAnterior(variaveisGlobais.getActivityAtual());
                getActivity().finish();
                break;
            case 4: //"Vagas"
                startActivity(new Intent(getActivity(), VagaActivity.class));
                variaveisGlobais.setActivityAnterior(variaveisGlobais.getActivityAtual());
                getActivity().finish();
                break;
            case 5: //"Mensagens"
                startActivity(new Intent(getActivity(), MensagemActivity.class));
                variaveisGlobais.setActivityAnterior(variaveisGlobais.getActivityAtual());
                getActivity().finish();
                break;
            case 6: //"Configuracoes"
                startActivity(new Intent(getActivity(), ConfiguracoesActivity.class));
                variaveisGlobais.setActivityAnterior(variaveisGlobais.getActivityAtual());
                getActivity().finish();
                break;
            case 7: //"Sair"
                AlertDialog.Builder alert = new AlertDialog.Builder(variaveisGlobais.getAlert());
                alert.setTitle("Sair");
                alert.setCancelable(false);
                alert.setMessage(R.string.alertsair);
                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                });
                alert.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();
                break;
             default:
                break;
        }

    }

}
