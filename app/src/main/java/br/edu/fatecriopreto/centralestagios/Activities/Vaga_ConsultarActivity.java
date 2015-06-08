package br.edu.fatecriopreto.centralestagios.Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import br.edu.fatecriopreto.centralestagios.Entidades.Beneficio;
import br.edu.fatecriopreto.centralestagios.Entidades.Candidato;
import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.Entidades.Vaga;
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
    private Button btnMensagem;
    private String emailEmpresa = "";
    private String vagaId = "";
    private String vagaDescricao = "";



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
        btnMensagem = (Button) findViewById(R.id.btnMensagem);

        btnCandidatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String url = variaveisGlobais.EndIPAPP + "/candidatar.aspx?rm=" + variaveisGlobais.getUserRm()+ "&vaga=" + vagaId;
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                JsonObjectRequest getRequest =
                        new JsonObjectRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        try {
                                            Log.d("Candidatou-se: ok",null);

                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.d("Error.Response", volleyError.getMessage());
                            }
                        });

                queue.add(getRequest);

                Toast.makeText(Vaga_ConsultarActivity.this, "Candidatou-se com sucesso!", Toast.LENGTH_LONG).show();
                btnCandidatar.setBackgroundColor(getResources().getColor(R.color.colorGrayWhite));
                btnCandidatar.setEnabled(false);
                //btnMensagem.set
                candidatar();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        emailEmpresa = bundle.getString("email");
        vagaId =  bundle.getString("id");
        vagaDescricao = bundle.getString("titlevaga");
        if (Boolean.parseBoolean(bundle.getString("Candidate"))) {
            btnCandidatar.setBackgroundColor(getResources().getColor(R.color.colorGrayWhite));
            btnCandidatar.setEnabled(false);
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

    public void candidatar(){


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailEmpresa});
        emailIntent.putExtra(Intent.EXTRA_CC, new String[]{"estagiosfatecriopreto@gmail.com"});
        //emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{"emailcentralestagios@email.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, vagaId + " - " + vagaDescricao);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Segue abaixo o curriculo do candidato: "+variaveisGlobais.perfilRm.getNome());

        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent,"Escolha seu aplicativo de email..."));
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
