package br.edu.fatecriopreto.centralestagios;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;

import br.edu.fatecriopreto.centralestagios.Activities.LoginActivity;
import br.edu.fatecriopreto.centralestagios.Activities.MainActivity;

public class variaveisGlobais extends Application {

    private static Class activityAnterior = LoginActivity.class;

    private static Class activityAtual = LoginActivity.class;

   private static MainActivity alert;

    public static Class getActivityAnterior(){
        return activityAnterior;
    }
    public static void setActivityAnterior(Class s){
       activityAnterior = s;
    }

    public static Class getActivityAtual() {
        return activityAtual;
    }

    public static void setActivityAtual(Class s) {
        variaveisGlobais.activityAtual = s;
    }

    public static MainActivity getAlert() {
        return alert;
    }

    public static void setAlert(MainActivity alert) {
        variaveisGlobais.alert = alert;
    }
}
