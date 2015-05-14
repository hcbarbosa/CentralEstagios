package br.edu.fatecriopreto.centralestagios;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.widget.RelativeLayout;

import br.edu.fatecriopreto.centralestagios.Activities.ConfiguracoesActivity;
import br.edu.fatecriopreto.centralestagios.Activities.LoginActivity;
import br.edu.fatecriopreto.centralestagios.Activities.MainActivity;

public class variaveisGlobais extends Application {

    private static Class activityAnterior = LoginActivity.class;

    private static Class activityAtual = LoginActivity.class;

    private static int drawer_user = R.layout.drawer_header;

    private static int imageUser = R.drawable.app_icon;

   private static Activity alert;

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

    public static Activity getAlert() {
        return alert;
    }

    public static void setAlert(Activity alert) {
        variaveisGlobais.alert = alert;
    }

    public static int getImageUser() {
        return imageUser;
    }

    public static void setImageUser(int imageUser) {
        variaveisGlobais.imageUser = imageUser;
    }

    public static int getDrawer_user() {
        return drawer_user;
    }

    public static void setDrawer_user(int drawer_user) {
        variaveisGlobais.drawer_user = drawer_user;
    }
}
