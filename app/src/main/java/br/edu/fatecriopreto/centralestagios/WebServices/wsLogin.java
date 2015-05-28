package br.edu.fatecriopreto.centralestagios.WebServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import br.edu.fatecriopreto.centralestagios.Utils.CustomVolleyRequestQueue;

public class wsLogin  extends AsyncTask<Void,Void,JSONArray>{

        String url;// = "http://centralestagios.ddns.net:8080/WebServices/Login.aspx?rm="+login+"&senha="+senha;
        Context context;
        RequestQueue queue;// = Volley.newRequestQueue(context);
        public static final String REQUEST_TAG = "VolleyBlockingRequestActivity";

        public wsLogin(String url, Context c, RequestQueue mQueue) {
            this.url = url;
            this.context = c;
            this.queue = mQueue;
        }

        public static String resposta;

        @Override
        protected JSONArray doInBackground(Void... params) {
            final RequestFuture<JSONArray> futureRequest = RequestFuture.newFuture();
            queue = CustomVolleyRequestQueue.getInstance(context.getApplicationContext()).getRequestQueue();

        JsonArrayRequest req = new JsonArrayRequest(url,new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                            try {
                                Log.d("RESPOSTA", response.toString());

                                JSONObject jo = response.getJSONObject(0);
                                resposta = jo.getString("resposta");

                                Log.d("RESPOSTA", resposta);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response.Aki", error.getMessage().toString());
                    }
        });

            req.setTag(REQUEST_TAG);
            queue.add(req);
                try{
                    futureRequest.get(14, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }

            return null;
        }


}
