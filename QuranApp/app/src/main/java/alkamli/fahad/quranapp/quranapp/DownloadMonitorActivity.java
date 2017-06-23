package alkamli.fahad.quranapp.quranapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import alkamli.fahad.quranapp.quranapp.adapter.DownloadAllAdapter;

public class DownloadMonitorActivity extends AppCompatActivity {

    private Activity activity;
    public boolean stopAll=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_monitor);
        activity=this;
       // if (DownloadManagement.active== false)
        {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    if (Looper.myLooper() == null)
                    {
                        Looper.prepare();
                    }
                    DownloadManagement manager=new DownloadManagement(CommonFunctions.getSourahList(getApplicationContext()),activity);
                }
            }).start();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.download_all_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        try {
            switch (item.getItemId())
            {

                case R.id.settings:
                {
                    Intent i = new Intent(getApplicationContext(), UserSettingsActivity.class);
                    startActivity(i);
                    //overridePendingTransition(R.anim.fadeout, R.anim.fadein);
                    return true;
                }
                case R.id.cancelAll:
                {
                    Log.d("Alkamli","cancelAll");
                    stopAll=true;
                    return true;
                }

            }
        }catch(Exception e)
        {
            class Local {};
            Log.e(CommonFunctions.TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));
        }

        return true;
    }


}
