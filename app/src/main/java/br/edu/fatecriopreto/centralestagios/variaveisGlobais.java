package br.edu.fatecriopreto.centralestagios;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Activities.ConfiguracoesActivity;
import br.edu.fatecriopreto.centralestagios.Activities.LoginActivity;
import br.edu.fatecriopreto.centralestagios.Activities.MainActivity;

public class variaveisGlobais extends Application {

    private static List<Class> activityAnterior = new ArrayList<>();

    private static int drawer_user = R.layout.drawer_header;

    private static int imageUser = R.drawable.app_icon;

   private static Activity alert;

    public static String KEY_ID ="id";
    public static String KEY_TITLE = "titlevaga";
    public static String KEY_SALARY = "salary";
    public static String KEY_COMPANY = "company";

    public static Class getActivityAnterior(int n){
        return activityAnterior.get(n);
    }

    public static void setActivityAnterior(Class s){
       activityAnterior.add(s);
    }

    public static int getSizeActivityAnterior(){
        return activityAnterior.size();
    }

    public static Activity getAlert() {
        return alert;
    }

    public static void setAlert(Activity alert) {
        variaveisGlobais.alert = alert;
    }

    public static void setImageUser(int imageUser) {
        variaveisGlobais.imageUser = imageUser;
    }

    public static void deleteAnterior() {
        activityAnterior.remove(getSizeActivityAnterior()-1);
    }

    public static void clearAnterior(){
        activityAnterior.clear();
    }
}


