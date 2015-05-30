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

import br.edu.fatecriopreto.centralestagios.Banco.DBAdapter;
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
                alert.setMessage(getResources().getString(R.string.duvidasBody1) + "\n"
                        + getResources().getString(R.string.duvidasBody2) + "\n"
                        + getResources().getString(R.string.duvidasBody3) + "\n"
                        + getResources().getString(R.string.duvidasBody4) + "\n"
                        + getResources().getString(R.string.duvidasBody5) + "\n");
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
        rm = mRmView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = true;
        View focusView = mRmView;

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

            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //chama o webservice
                respostaws = wsLogin.verificaLoginJson(rm, password, getApplicationContext());
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //chama o webservice
                            respostaws = wsLogin.verificaLoginJson(rm, password, getApplicationContext());

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }

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
                        break;
                }
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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }



    //Pega o evento de voltar do celular e volta para a activity anterior
    public void onBackPressed(){
       this.finish();
    }
}