package br.edu.fatecriopreto.centralestagios.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import br.edu.fatecriopreto.centralestagios.Banco.DBAdapter;
import br.edu.fatecriopreto.centralestagios.Entidades.Login;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.WebServices.*;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;


public class LoginActivity extends Activity  {

    private EditText mRmView;
    private EditText mPasswordView;
    private TextView mtxtDuvidas;
    private CheckBox mRmRemember;

    private ProgressBar mProgressBar;

    public  String respostaws = "";
    public   boolean cancel = true;
    public View focusView = mRmView;
    private RequestQueue queue;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRmView = (EditText) findViewById(R.id.edtRm);
        mPasswordView = (EditText) findViewById(R.id.edtSenha);

        //se ja existe algum rm armazenado
        DBAdapter db = new DBAdapter(LoginActivity.this);
        db.open();
        if(db.getPerfilRememberRm() != null){
            mRmRemember = (CheckBox) findViewById(R.id.chkLembraRm);
            String lembraRm = String.valueOf(db.getPerfilRememberRm().getColumnIndex("rememberRm"));
            //String lembraRm = "123456789068";
            if(!lembraRm.equals("") && !lembraRm.equals("0")){
                mRmView.setText(lembraRm);
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
                alert.setCancelable(false);
                final View recipientsLayout = getLayoutInflater().inflate(R.layout.scrollable_alert, null);
                final TextView recipientsTextView = (TextView) recipientsLayout.findViewById(R.id.textDuvidas);

                recipientsTextView.setText(getResources().getString(R.string.duvidasBody1) + "\n"
                        + getResources().getString(R.string.duvidasBody2) + "\n"
                        + getResources().getString(R.string.duvidasBody3) + "\n"
                        + getResources().getString(R.string.duvidasBody4) + "\n"
                        + getResources().getString(R.string.duvidasBody5) + "\n");
                alert.setView(recipientsLayout);

                alert.setIcon(R.drawable.help_circle);
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
        final String rm = mRmView.getText().toString();
        final String password = mPasswordView.getText().toString();



        // rm vazio
        if (TextUtils.isEmpty(rm)) {
            mRmView.setError(getString(R.string.error_field_required));
            focusView = mRmView;
            cancel = true;
        //senha vazia
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        //tamanho da senha
        } else if (password.length() < 2) {
            mPasswordView.setError(getString(R.string.error_short_senha));
            focusView = mPasswordView;
            cancel = true;
        } else {

            mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

            // chama progress bar
            mProgressBar.setVisibility(ProgressBar.GONE);


            //chama o webservice

            respostaws = "Sem conexao";

            //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            //StrictMode.setThreadPolicy(policy);
            //chama o webservice
            //respostaws = wsLogin.verificaLoginJson(rm, password, getApplicationContext());

            url = "http://192.168.26.50:26046/WebServices/Login.aspx?rm=" + rm + "&senha=" + password;
            Thread threadA = new Thread() {
                public void run() {
                    wsLogin threadB = new wsLogin(url, LoginActivity.this, queue);
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = threadB.execute().get(15, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                    final JSONArray receivedJSONArray = jsonArray;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Resposta is: ", String.valueOf(wsLogin.resposta.toString()));
                            respostaws = wsLogin.resposta.toString();

                        }//fecha run
                    });
                }
            };

            threadA.start();

        }//fecha else e verifica

        if (respostaws != null) {
            //verifica se a resposta foi possitiva e existe aquele login na base de dados, ou qual erro deu
            switch (respostaws) {
                case "Sem conexao":
                    mRmView.setError("Sem acesso a base de dados");
                    mRmView.setText("");
                    mPasswordView.setText("");
                    focusView = mRmView;
                    cancel = true;
                    break;
                case "rm":
                    mRmView.setError(getString(R.string.error_invalid_rm));
                    focusView = mRmView;
                    cancel = true;
                    break;
                case "senha":
                    mPasswordView.setError(getString(R.string.error_invalid_senha));
                    focusView = mPasswordView;
                    cancel = true;
                    break;
                case "acesso":
                    mRmView.setError(getString(R.string.error_deny_access));
                    mRmView.setText("");
                    mPasswordView.setText("");
                    focusView = mRmView;
                    cancel = true;
                    break;
                case "ok":
                    cancel = false;
                    break;
            }
        }

        //verifica se eh necessario cancelar e da foco no que esta errado
        if (cancel) {
            focusView.requestFocus();
        } else {
            //armazena o rm se estiver checkado
            if(mRmRemember.isChecked()){
                Toast.makeText(LoginActivity.this, "Rm:" + rm + " foi armazenado" , Toast.LENGTH_LONG).show();
            }
            //chama a main
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }

    }



    //Pega o evento de voltar do celular e volta para a activity anterior
    public void onBackPressed(){
       this.finish();
    }
}