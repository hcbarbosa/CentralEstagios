package br.edu.fatecriopreto.centralestagios.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.WebServices.*;

public class LoginActivity extends Activity  {

    private EditText mRmView;
    private EditText mPasswordView;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRmView = (EditText) findViewById(R.id.edtRm);
        mPasswordView = (EditText) findViewById(R.id.edtSenha);

        Button mEmailSignInButton = (Button) findViewById(R.id.btnEntrar);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    public void attemptLogin() {

        // Reseta erros.
        mRmView.setError(null);
        mPasswordView.setError(null);

        // Armazena os valores da tela em variaveis
        final String rm = mRmView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = true;
        View focusView = null;

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
            //mostra uma mensagem ao usuario para esperar
            progressDialog = ProgressDialog.show(this, "", "Aguarde, verificando login na base de dados...", true);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //chama o webservice
            String respostaws = wsLogin.verificaLoginSoap(rm, password, getApplicationContext());
            if (respostaws != null) {
                //verifica se a resposta foi possitiva e existe aquele login na base de dados, ou qual erro deu
                switch (respostaws){
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
                    case "true":
                        cancel = false;
                        break;
                }
            }

        }
        progressDialog.dismiss();
        //verifica se é necessario cancelar e da foco no que esta errado
        if (cancel) {
            focusView.requestFocus();
        } else {
            //chama a main
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}