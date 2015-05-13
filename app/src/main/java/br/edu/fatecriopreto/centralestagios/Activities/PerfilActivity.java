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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Tabs.SlidingTabLayout;
import br.edu.fatecriopreto.centralestagios.Utils.FloatingActionButton;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;


public class PerfilActivity extends ActionBarActivity {

    private Toolbar appBar;
    TextView txtDadosPessoais;
    private static FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior() != PerfilActivity.class)
            variaveisGlobais.setActivityAtual(PerfilActivity.class);
        variaveisGlobais.setAlert(PerfilActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);

        //Ligas as variaveis com a tela
        txtDadosPessoais = (TextView) findViewById(R.id.txtDadosPessoais);
        //Animacao no txt
        txtDadosPessoais.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));

        //botao flutuante
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_edit);

        floatingButton = new FloatingActionButton.Builder(this)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .setContentView(imageView).build();

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PerfilActivity.this, PerfilEditActivity.class));
                finish();
            }
        });

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

    //Pega o evento de voltar do celular e volta para a activity anterior
    public void onBackPressed(){
        startActivity(new Intent(this, variaveisGlobais.getActivityAnterior()));
        this.finish();
    }

    //esconde o botao quando aparece menu
    public static void onDrawerSlide(float slideOffset) {
        if(floatingButton != null){
           floatingButton.setTranslationX(slideOffset * 200);
        }
    }
}