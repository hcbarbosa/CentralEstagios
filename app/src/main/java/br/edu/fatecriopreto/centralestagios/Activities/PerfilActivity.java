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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

import java.util.ArrayList;

import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.Entidades.Curso;
import br.edu.fatecriopreto.centralestagios.Entidades.Perfil;
import br.edu.fatecriopreto.centralestagios.Menu.NavigationDrawerFragment;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.Tabs.SlidingTabLayout;
import br.edu.fatecriopreto.centralestagios.Utils.FloatingActionButton;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;


public class PerfilActivity extends ActionBarActivity {

    private Toolbar appBar;
    TextView txtDadosPessoais;
    private static FloatingActionButton floatingButton;

    TextView txtNome;
    TextView txtEmail;
    TextView txtTelefone;
    TextView txtAno;
    TextView txtSemestre;
    TextView txtCurso;
    TextView txtCep;
    TextView txtLogradouro;
    TextView txtComplemento;
    TextView txtBairro;
    TextView txtCidade;
    TextView txtUf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtTelefone = (TextView) findViewById(R.id.txtTelefone);
        txtAno = (TextView) findViewById(R.id.txtAno);
        txtSemestre = (TextView) findViewById(R.id.txtSemestre);
        txtCurso = (TextView) findViewById(R.id.txtCurso);
        txtCep = (TextView) findViewById(R.id.txtCep);
        txtLogradouro = (TextView) findViewById(R.id.txtLogradouro);
        txtComplemento = (TextView) findViewById(R.id.txtComplemento);
        txtBairro = (TextView) findViewById(R.id.txtBairro);
        txtCidade = (TextView) findViewById(R.id.txtCidade);
        txtUf = (TextView) findViewById(R.id.txtUf);

        txtNome.setText(variaveisGlobais.getUserName());
        txtEmail.setText(variaveisGlobais.perfilRm.getEmail());
        txtTelefone.setText(variaveisGlobais.perfilRm.getTelefone());
        txtAno.setText(String.valueOf(variaveisGlobais.perfilRm.getAno()));
        txtSemestre.setText(String.valueOf(variaveisGlobais.perfilRm.getSemestre()));
        txtCep.setText(variaveisGlobais.perfilRm.getCEP());
        txtLogradouro.setText(variaveisGlobais.perfilRm.getLogradouro());
        txtComplemento.setText(variaveisGlobais.perfilRm.getComplemento());
        txtBairro.setText(variaveisGlobais.perfilRm.getBairro());
        txtCidade.setText(variaveisGlobais.perfilRm.getCidade());
        txtUf.setText(variaveisGlobais.perfilRm.getUf());

        if(variaveisGlobais.listCursos == null) {

            variaveisGlobais.listCursos = new ArrayList<>();

            final String url = variaveisGlobais.EndIPAPP + "/cursos.aspx";

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            JsonArrayRequest getRequest =
                    new JsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    Curso curso;

                                    for (int i = 0; i < response.length(); i++) {

                                        curso = new Curso();

                                        try {
                                            curso.setDescricao(response.getJSONObject(i).getString("Descricao"));
                                            curso.setId(response.getJSONObject(i).getInt("Id"));

                                            variaveisGlobais.listCursos.add(curso);

                                            for (Curso aux : variaveisGlobais.listCursos){

                                                if(aux.getId() == variaveisGlobais.perfilRm.getCursoId()){

                                                    txtCurso.setText(aux.getDescricao());
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("Error.Response", "erro no webservice de perfil");
                        }
                    });

            queue.add(getRequest);
        }
        else {

            for (Curso aux : variaveisGlobais.listCursos){

                if(aux.getId() == variaveisGlobais.perfilRm.getCursoId()){

                    txtCurso.setText(aux.getDescricao());
                }
            }
        }

        //Auxiliar na transicao de telas e pilha
        if(variaveisGlobais.getActivityAnterior((variaveisGlobais.getSizeActivityAnterior()-1)) != PerfilActivity.class)
            variaveisGlobais.setActivityAnterior(PerfilActivity.class);
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
        //txtDadosPessoais.setAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));

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
        startActivity(new Intent(this, variaveisGlobais.getActivityAnterior(variaveisGlobais.getSizeActivityAnterior()-2)));
        variaveisGlobais.deleteAnterior();
        this.finish();
    }

    //esconde o botao quando aparece menu
    public static void onDrawerSlide(float slideOffset) {
        if(floatingButton != null){
           floatingButton.setTranslationX(slideOffset * 200);
        }
    }
}