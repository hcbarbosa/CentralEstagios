package br.edu.fatecriopreto.centralestagios.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.edu.fatecriopreto.centralestagios.Banco.DBAdapter;
import br.edu.fatecriopreto.centralestagios.Banco.DBHelper;
import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.Entidades.Perfil;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;


public class LoginActivity extends Activity  {

    private EditText mRmView;
    private EditText mPasswordView;
    private TextView mtxtDuvidas;
    private CheckBox mRmRemember;

    private ProgressBar mProgressBar;

    public  String respostaws = "";
    public   boolean cancel = true;
    public View focusView = null;

    String rm;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRmView = (EditText) findViewById(R.id.edtRm);
        mPasswordView = (EditText) findViewById(R.id.edtSenha);

        rm = "";
        password = "";

        //se ja existe algum rm armazenado
        DBAdapter db = new DBAdapter(LoginActivity.this);
        db.open();

        Log.d("create: ", String.valueOf(db.retornarNotificacaoTempo()));

        //Log.d("rm: ", String.valueOf(db.getRM().getInt()));

        if(db.getRM() != null){
            mRmRemember = (CheckBox) findViewById(R.id.chkLembraRm);
            String lembraRm = String.valueOf(db.retornarRm().getRm());
            //String lembraRm = "123456789068";
            if(!lembraRm.equals("0")){
                mRmView.setText(lembraRm);
                focusView = mPasswordView;
                focusView.requestFocus();
                mRmRemember.setChecked(true);
            }else
            {
                mRmRemember.setChecked(false);
            }
        }
        db.close();


        //Rede de dados
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo cNetInfo = cManager.getActiveNetworkInfo();
        //Wifi
        NetworkInfo wNetInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        Button mEmailSignInButton = (Button) findViewById(R.id.btnEntrar);

        //verifica se existe alguma conexao de internet
        if((cNetInfo != null && cNetInfo.isConnected()) || (wNetInfo != null && wNetInfo.isConnected())) {
            mEmailSignInButton.setEnabled(true);
        }
        else{
            Toast.makeText(LoginActivity.this, "Internet não está disponível\nNão conseguirá acessar a base", Toast.LENGTH_LONG).show();
            mEmailSignInButton.setEnabled(false);
        }

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mtxtDuvidas = (TextView) findViewById(R.id.btnDuvidas);
        mtxtDuvidas.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                alert.setTitle(R.string.duvidasTitle);
                alert.setIcon(R.drawable.app_icon);
                alert.setCancelable(false);
                final View recipientsLayout = getLayoutInflater().inflate(R.layout.scrollable_alert, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.textDuvidas);
                recipientsTextView.setText(getResources().getString(R.string.duvidasBody0).toUpperCase() + "\n" + "\n"
                                + getResources().getString(R.string.duvidasBody1) + "\n"
                                + getResources().getString(R.string.duvidasBody2) + "\n"
                                + getResources().getString(R.string.duvidasBody3) + "\n" + "\n"
                                + getResources().getString(R.string.duvidasBody4).toUpperCase() + "\n" + "\n"
                                + getResources().getString(R.string.duvidasBody5) + "\n"
                                + getResources().getString(R.string.duvidasBody6) + "\n"
                                + getResources().getString(R.string.duvidasBody7) + "\n" + "\n"
                                + getResources().getString(R.string.duvidasBody8).toUpperCase() + "\n" + "\n"
                                + getResources().getString(R.string.duvidasBody9) + "\n"
                                + getResources().getString(R.string.duvidasBody10) + "\n"
                                + getResources().getString(R.string.duvidasBody11) + "\n"
                                + getResources().getString(R.string.duvidasBody12) + "\n" + "\n"
                                + getResources().getString(R.string.duvidasBody13).toUpperCase() + "\n" + "\n"
                                + getResources().getString(R.string.duvidasBody14) + "\n"
                                + getResources().getString(R.string.duvidasBody15) + "\n"
                                + getResources().getString(R.string.duvidasBody16) + "\n");
                alert.setIcon(R.drawable.help_circle);
                alert.setView(recipientsLayout);
                alert.setNeutralButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

        variaveisGlobais.clearAnterior();
        if(variaveisGlobais.getSizeActivityAnterior()!=0 &&
                variaveisGlobais.getActivityAnterior(variaveisGlobais.getSizeActivityAnterior()-1)!= LoginActivity.class)
            variaveisGlobais.setActivityAnterior(LoginActivity.class);
        else{
            variaveisGlobais.setActivityAnterior(LoginActivity.class);
        }

    }

    public void attemptLogin() {

        // Reseta erros.
        mRmView.setError(null);
        mPasswordView.setError(null);

        // Armazena os valores da tela em variaveis
        rm = mRmView.getText().toString();
        password = mPasswordView.getText().toString();

        // rm vazio
        if (TextUtils.isEmpty(rm)) {
            mRmView.setError(getString(R.string.error_field_required));
            focusView = mRmView;
            focusView.requestFocus();
            //senha vazia
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            focusView.requestFocus();
            //tamanho da senha
        } else if (password.length() < 2) {
            mPasswordView.setError(getString(R.string.error_short_senha));
            focusView = mPasswordView;
            focusView.requestFocus();
        } else {//tudo preenchido

            mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

            // chama progress bar
            mProgressBar.setVisibility(ProgressBar.GONE);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);

            //chama o webservice
            respostaws = "Sem conexao";

            //chama o webservice
            String url = variaveisGlobais.EndIPAPP+"/Login.aspx?rm="+rm+"&senha="+password;
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            JsonArrayRequest getRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {

                            try {
                                JSONObject jo = response.getJSONObject(0);
                                respostaws = jo.getString("resposta");
                                Log.d("RESPOSTA", jo.toString());

                                if (respostaws != null) {
                                    //verifica se a resposta foi possitiva e existe aquele login na base de dados, ou qual erro deu
                                    switch (respostaws) {
                                        case "Sem conexao":
                                            mRmView.setError("Sem acesso a base de dados");
                                            mRmView.setText("");
                                            mPasswordView.setText("");
                                            rm = "";
                                            password = "";
                                            focusView = mRmView;
                                            cancel = true;
                                            break;
                                        case "rm":
                                            mRmView.setError(getString(R.string.error_invalid_rm));
                                            focusView = mRmView;
                                            rm = "";
                                            password = "";
                                            cancel = true;
                                            break;
                                        case "senha":
                                            mPasswordView.setError(getString(R.string.error_invalid_senha));
                                            focusView = mPasswordView;
                                            rm = "";
                                            password = "";
                                            cancel = true;
                                            break;
                                        case "acesso":
                                            mRmView.setError(getString(R.string.error_deny_access));
                                            mRmView.setText("");
                                            mPasswordView.setText("");
                                            rm = "";
                                            password = "";
                                            focusView = mRmView;
                                            cancel = true;
                                            break;
                                        case "ok":
                                            cancel = false;
                                            variaveisGlobais.setUserEmail(jo.getString("email"));
                                            variaveisGlobais.setUserRm(jo.getString("rm"));
                                            variaveisGlobais.setUserName(jo.getString("nome"));

                                            String url2 = variaveisGlobais.EndIPAPP+"/perfil.aspx?rm="+variaveisGlobais.getUserRm()+"&acao=obter";
                                            RequestQueue fila = Volley.newRequestQueue(getApplicationContext());

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

                                            break;
                                        case "requerido":
                                            cancel = false;
                                            variaveisGlobais.setUserRm(jo.getString("rm"));
                                            break;
                                    }

                                    //verifica se eh necessario cancelar e da foco no que esta errado
                                    if (cancel) {
                                        focusView.requestFocus();
                                    } else {
                                        //armazena o rm se estiver checkado
                                        if (mRmRemember.isChecked()) {

                                            DBAdapter db = new DBAdapter(LoginActivity.this);
                                            db.open();
                                            if(db.retornarRm().getRm() == 0) {

                                                Toast.makeText(LoginActivity.this, "O RM '" + rm + "' foi armazenado", Toast.LENGTH_LONG).show();

                                                db.apagarRms();
                                                db.adicionar(Integer.parseInt(rm), 1);
                                                db.close();
                                            }
                                            else if(!String.valueOf(db.retornarRm().getRm()).equals(rm)) {

                                                Toast.makeText(LoginActivity.this, "O RM '" + rm + "' foi armazenado", Toast.LENGTH_LONG).show();

                                                db.apagarRms();
                                                db.adicionar(Integer.parseInt(rm), 1);
                                                db.close();
                                            }
                                        }
                                        else {

                                            DBAdapter db = new DBAdapter(LoginActivity.this);
                                            db.open();

                                            if (db.getRM() != null) {
                                                db.apagarRms();
                                                db.close();
                                            }
                                        }
                                        //chama a main
                                        if (respostaws.equals("ok")) {
                                            Intent intent = new Intent(RetornaAc(), MainActivity.class);
                                            startActivity(intent);
                                            FecharLogin();
                                        } else if (respostaws.equals("requerido")) {
                                            Intent intent = new Intent(RetornaAc(), PerfilRequeridoActivity.class);
                                            startActivity(intent);
                                            FecharLogin();
                                        }
                                    }
                                    focusView = null;
                                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                                    respostaws = null;

                                }//fecha se resposta eh diferente de null
                                else{

                                        mRmView.setError("Sem acesso a base de dados");
                                        mRmView.setText("");
                                        mPasswordView.setText("");
                                        rm = "";
                                        password = "";
                                        focusView = mRmView;
                                        focusView.requestFocus();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Error.Response.Aki", "erro");
                }
            });

            queue.add(getRequest);

        }//fecha dados preenchidos corretamente
    }//fecha metodo attemplogin


    public void FecharLogin(){
        this.finish();
    }

    public LoginActivity RetornaAc(){
        return  this;
    }

    //Pega o evento de voltar do celular e volta para a activity anterior
    public void onBackPressed(){
       this.finish();
    }
}