package br.edu.fatecriopreto.centralestagios.WebServices;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class wsLogin {

    private static final String NAMESPACE = "http://centralestagios.org/";
    private static final String METHOD_NAME = "verificaLogin";
    private static final String SOAP_ACTION = "http://centralestagios.org/verificaLogin";
    private static final String URL = "http://192.168.0.18/Login.asmx";

    public static String verificaLoginSoap(String login, String senha){
        String resposta = "Sem conexao";

        //Objeto composto pelo NameSpace e pelo método que queremos chamar
        SoapObject soap = new SoapObject(NAMESPACE, METHOD_NAME);

        //Adicionando parametros
        soap.addProperty("login",login);
        soap.addProperty("senha", senha);

        //Cria um objeto serializado, um envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //Diz que o webservice utilizado é um .NET
        envelope.dotNet = true;

        //Envia a requisicao ao webservice
        envelope.setOutputSoapObject(soap);

        //Cria a comunicacao com o webservice do site
        HttpTransportSE httpTransport = new HttpTransportSE(URL);

        try {
            //chama o webservice e passa o nome do namespace da aplicacao Asp .Net mais o nome do metodo e o envelope
            httpTransport.call(SOAP_ACTION, envelope);

            //transforma a resposta em um objeto
            Object retorno = envelope.getResponse();

            //escreve no terminal
            Log.d("wsLogin", "Retorno: " + retorno.toString());
            resposta = retorno.toString();

        } catch (SoapFault e) {
           // e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (XmlPullParserException e) {
            //e.printStackTrace();
        }
        return resposta;
    }

    /*
    public static String verificaLoginJson(String login, String senha) {
        String resposta = "Sem conexao";




        return resposta;
    }
    */
}
