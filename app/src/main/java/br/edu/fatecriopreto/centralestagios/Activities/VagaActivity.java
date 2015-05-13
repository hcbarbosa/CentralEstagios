package br.edu.fatecriopreto.centralestagios.Activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class VagaActivity extends ActionBarActivity {


    private Toolbar appBar;
    private Button btnCandidatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaga);

        //Auxiliar na transicao de telas e pilha
        variaveisGlobais.setActivityAtual(VagaActivity.class);
        variaveisGlobais.setAlert(VagaActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);


        btnCandidatar = (Button) findViewById(R.id.btnCandidatar);
        btnCandidatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candidatar();
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
            variaveisGlobais.setActivityAnterior(VagaActivity.class);
            variaveisGlobais.setActivityAtual(VagaActivity.class);
            this.finish();
        }else if (id == R.id.pesqavancada) {
            startActivity(new Intent(this, VagaPesquisaAvancadaActivity.class));
            variaveisGlobais.setActivityAnterior(VagaActivity.class);
            variaveisGlobais.setActivityAtual(VagaActivity.class);
            this.finish();
        }else if (id == R.id.pesqrecomendada) {
            startActivity(new Intent(this, VagaRecomendadaActivity.class));
            variaveisGlobais.setActivityAnterior(VagaActivity.class);
            variaveisGlobais.setActivityAtual(VagaActivity.class);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Pega o evento de voltar do celular e volta para a activity anterior
    public void onBackPressed(){
        startActivity(new Intent(this, variaveisGlobais.getActivityAnterior()));
        this.finish();
    }
}
