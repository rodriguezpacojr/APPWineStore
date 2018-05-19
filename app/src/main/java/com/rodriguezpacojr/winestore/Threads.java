package com.rodriguezpacojr.winestore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.util.Set;

/**
 * Created by francisco on 3/9/18.
 */

public class Threads extends AsyncTask<Void, Integer, Void> {
    private ProgressBar pbwelcome;
    private Context context;
    private Activity activity;

    public Threads(ProgressBar pbwelcome, Context context, Activity activity) {
        this.pbwelcome = pbwelcome;
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Setup setup = new Setup();
        Intent intInicio;

        if (setup.getUserName().equals("admin"))
            intInicio = new Intent(context, HomeAdminActivity.class);
        else
            intInicio = new Intent(context, RoutesActivity.class);

        intInicio.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intInicio);
        activity.finish();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pbwelcome.setProgress(values[0]);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            for (int i=1; i<=5; i++) {
                publishProgress(i * 20);
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}