package br.edu.fatecriopreto.centralestagios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import br.edu.fatecriopreto.centralestagios.Activities.ConfiguracoesActivity;
import br.edu.fatecriopreto.centralestagios.Activities.LoginActivity;
import br.edu.fatecriopreto.centralestagios.Activities.VagaActivity;

/**
 * Created by Victor on 03/06/2015.
 */
public class notificacao {

    private long delayOuvidor = 6000;

    public void criarNotificacao(Context context, String mensagemBarraStatus, String titulo,
                                 String subtitulo, String mensagem, Class activity) {

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setSubText(subtitulo)
                .setTicker(mensagemBarraStatus);

        PendingIntent pendingIntent = PendingIntent.getActivity(variaveisGlobais.getActivityAtual(), 0, new Intent
                (variaveisGlobais.getActivityAtual(), activity), 0);

        nBuilder.setContentIntent(pendingIntent);

        nBuilder.setVibrate(new long[]{100, 250, 100, 500});

        int idNotification = 001;

        NotificationManager notificationManager =
                (NotificationManager) variaveisGlobais.getActivityAtual().getSystemService
                        (variaveisGlobais.getActivityAtual().NOTIFICATION_SERVICE);

        notificationManager.notify(idNotification, nBuilder.build());
    }

    public Handler handler = new Handler();

    public Runnable ouvidorNotificacoes = new Runnable() {
        @Override
        public void run() {

            String url = variaveisGlobais.EndIPAPP+"/notificacoes.aspx?rm=" + variaveisGlobais.getUserRm() +
                    "&acao=listar";
            RequestQueue queue = Volley.newRequestQueue(variaveisGlobais.getActivityAtual());

            JsonObjectRequest getRequest = new JsonObjectRequest(url, new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                if(response.getString("Conteudo").toString().equals("vaga")) {

                                    criarNotificacao(variaveisGlobais.getActivityAtual(),
                                            "Nova vaga", "Nova vaga", "Nova vaga que combina com seu perfil",
                                            "Clique para ver", VagaActivity.class);
                                }
                            }
                            catch (Exception e){

                                Log.d("Erro em notificacao: ", e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    Log.d("Erro no webservice em notificacao: ", volleyError.getMessage());
                }
            });

            queue.add(getRequest);

            handler.postDelayed(ouvidorNotificacoes, delayOuvidor);
        }
    };

    public void comecarOuvidorNotificacoes(){

        handler.postDelayed(ouvidorNotificacoes, delayOuvidor);
    }
}
