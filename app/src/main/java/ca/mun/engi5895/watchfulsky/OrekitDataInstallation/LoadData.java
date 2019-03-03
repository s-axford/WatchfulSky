package ca.mun.engi5895.watchfulsky.OrekitDataInstallation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoadData extends AsyncTask<Void, Void, Void> {
    private ProgressDialog progressDialog;
    private Context context;
    //declare other objects as per your need

    public LoadData(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        context = activity.getApplicationContext();
    }

    @Override
    protected void onPreExecute()
    {
        progressDialog.setMessage("Downloading Satellite Data from Celestrak");
        progressDialog.show();

        //do initialization of required objects objects here
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        celestrakData.downloadData(context);
        //do loading operation here
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        progressDialog.dismiss();
    }
}