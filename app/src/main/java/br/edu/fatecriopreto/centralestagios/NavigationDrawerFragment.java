package br.edu.fatecriopreto.centralestagios;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {

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
        adapter = new InformationAdapter(getActivity(),getListaMenu());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    public static List<Information> getListaMenu(){
        List<Information> listMenu = new ArrayList<>();
        int[] icons = {R.drawable.ic_plusone_medium_off_client,R.drawable.ic_plusone_medium_off_client,
                R.drawable.ic_plusone_medium_off_client,R.drawable.ic_plusone_medium_off_client,
                R.drawable.ic_plusone_medium_off_client,R.drawable.ic_plusone_medium_off_client,
                R.drawable.ic_plusone_medium_off_client};
        String[] titles = {"Inicio","Perfil","Curriculo","Vagas", "Mensagens","Configuracoes","Sair"};

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
                appBar.setTitle("Menu");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                appBar.setTitle("Central de Estágios");
            }


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //guarda a porcentagem do tamanho do menu ja percorrida
                //Log.d("CentralEstagios","offset "+slideOffset);
                if(slideOffset < 0.6) {
                    appBar.setAlpha(2 - slideOffset);
                }
            }
        };

        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(preferenceName,preferenceValue);
    }
}
