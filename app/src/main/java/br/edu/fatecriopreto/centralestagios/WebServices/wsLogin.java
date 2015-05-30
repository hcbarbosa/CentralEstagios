package br.edu.fatecriopreto.centralestagios.WebServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class wsLogin{

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String METHOD_NAME = "verificaLogin";
    private static final String SOAP_ACTION = "http://tempuri.org/verificaLogin";
    private static final String URL = "http://http://centralestagios.azurewebsites.net/WebServices/Login.asmx?WSDL";

    public static String resposta;

    public static String verificaLoginSoap(String login, String senha, Context applicationContext){
        String resposta = "Sem conexao";

        //Objeto composto pelo NameSpace e pelo metodo que queremos chamar
        SoapObject soap = new SoapObject(NAMESPACE, METHOD_NAME);

        //Adicionando parametros
        soap.addProperty("rm",login);
        soap.addProperty("senha", senha);

        //Cria um objeto serializado, um envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //Envia a requisicao ao webservice
        envelope.setOutputSoapObject(soap);

        //Diz que o webservice utilizado eh um .NET
        envelope.dotNet = true;



        try {
            //Cria a comunicacao com o webservice do site
            HttpTransportSE httpTransport = new HttpTransportSE(URL);

            //chama o webservice e passa o nome do namespace da aplicacao Asp .Net mais o nome do metodo e o envelope
            httpTransport.call(SOAP_ACTION, envelope);

            //transforma a resposta em um objeto
            SoapObject retorno = (SoapObject)envelope.getResponse();

            //escreve no terminal
            Log.d("wsLogin", "Retorno: " + retorno.toString());
            resposta = retorno.toString();

        } catch (Exception e) {
           e.printStackTrace();
        }
        return resposta;
    }

    public static String verificaLoginJson(String login, String senha, Context context) {

        String url = "http://192.168.0.101:26046/WebServices/Login.aspx?rm="+login+"&senha="+senha;
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest getRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                            try {
                                JSONObject jo = response.getJSONObject(0);
                                resposta = jo.getString("resposta");
                                Log.d("RESPOSTA", jo.toString());
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

        queue.add(getRequest);

            return resposta;
    }
}
