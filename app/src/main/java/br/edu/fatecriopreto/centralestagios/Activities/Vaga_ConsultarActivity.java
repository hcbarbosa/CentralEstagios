package br.edu.fatecriopreto.centralestagios.Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Tabs.SlidingTabLayout;
import br.edu.fatecriopreto.centralestagios.Utils.ListVagasAdapter;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class Vaga_ConsultarActivity extends ActionBarActivity {

    private Toolbar appBar;
    private TextView txtnome;
    private TextView txtempresa;
    private TextView txtsalario;
    private TextView txtperiodo;
    private TextView txthorario;
    private TextView txtemail;
    private TextView txtcontato;
    private TextView txttipovaga;
    private TextView txttelefone;
    private TextView txtobservacoes;
    private TextView txtbeneficios;
    private TextView txtconhecimentos;
    private Button btnCandidatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaga__consultar);

        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior() - 1)) != Vaga_ConsultarActivity.class)
            variaveisGlobais.setActivityAnterior(Vaga_ConsultarActivity.class);
        variaveisGlobais.setAlert(Vaga_ConsultarActivity.this);

        //AppBar
        appBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //DrawerFragment = menulateral
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), appBar);

        txtnome = (TextView) findViewById(R.id.txtNomeVaga);
        txtempresa = (TextView) findViewById(R.id.txtEmpresa);
        txtsalario = (TextView) findViewById(R.id.txtSalario);
        txtperiodo = (TextView) findViewById(R.id.txtPeriodo);
        txthorario = (TextView) findViewById(R.id.txtHorario);
        txtemail = (TextView) findViewById(R.id.txtEmail);
        txtcontato = (TextView) findViewById(R.id.txtContato);
        txttipovaga = (TextView) findViewById(R.id.txttipovaga);
        txttelefone = (TextView) findViewById(R.id.txtTelefone);
        txtobservacoes = (TextView) findViewById(R.id.txtObservacoes);
        txtbeneficios = (TextView) findViewById(R.id.txtBeneficios);
        txtconhecimentos = (TextView) findViewById(R.id.txtConhecimentos);
        btnCandidatar = (Button) findViewById(R.id.btnCandidatar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (variaveisGlobais.listVagas.get(Integer.parseInt(bundle.getString("position"))).isCandidatado()) {
            btnCandidatar.setVisibility(View.INVISIBLE);
        }
        if(bundle != null){
            this.setTitle(bundle.getString("titlevaga"));
            txtnome.setText(bundle.getString("titlevaga"));
            txtempresa.setText(bundle.getString("company"));
            txtsalario.setText(bundle.getString("salary"));
            txtcontato.setText(bundle.getString("contact"));
            txtemail.setText(bundle.getString("email"));
            txthorario.setText(bundle.getString("hour"));
            txtobservacoes.setText(bundle.getString("observation"));
            txttipovaga.setText(bundle.getString("type"));
            txttelefone.setText(bundle.getString("phone"));
            txtperiodo.setText(bundle.getString("periody"));
            txtbeneficios.setText(bundle.getString("beneficts"));
            txtconhecimentos.setText(bundle.getString("skills"));

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_vaga__consultar, menu);
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

        return super.onOptionsItemSelected(item);
    }

    //Pega o evento de voltar do celular e volta para a activity anterior
    public void onBackPressed(){
        startActivity(new Intent(this, variaveisGlobais.getActivityAnterior(variaveisGlobais.getSizeActivityAnterior()-2)));
        variaveisGlobais.deleteAnterior();
        this.finish();
    }
}
