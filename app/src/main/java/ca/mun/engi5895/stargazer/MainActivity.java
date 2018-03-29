package ca.mun.engi5895.stargazer;

import android.app.DialogFragment;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import ca.mun.engi5895.stargazer.ErrorDialogFragment;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FilenameUtils;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.zeroturnaround.zip.ZipUtil;
//import org.zeroturnaround.zip.commons.FilenameUtils;
//import org.zeroturnaround.zip.commons.FilenameUtils;
import org.apache.commons.io.FilenameUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Download data
        celestrakData.downloadData(this);

        //STAVOR CODE
        Install.installApkData(this);

        //Initialize Orekit with the data files
        OrekitInit.init(Install.getOrekitDataRoot(this));

    }


    /**
     * Displays an error dialog
     * @param message
     * @param canIgnore
     */
    public void showErrorDialog(String message, boolean canIgnore) {
        DialogFragment newFragment = ErrorDialogFragment.newInstance(message, canIgnore);
        newFragment.setCancelable(false);
        newFragment.show(getFragmentManager(), "error");
    }

    public void geoGo(View view) {
        Intent intent = new Intent(this, activity_satellite_sel.class);
        startActivity(intent);
    }

    public void helioGo(View view) {
        Intent intent = new Intent(this, HeliocentricActivity.class);
        startActivity(intent);
    }

    public void settingsGo(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void areoGo(View view) {
        Intent intent = new Intent(this, AreocentricActivity.class);
        startActivity(intent);
    }
}
