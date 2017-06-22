package alkamli.fahad.quranapp.quranapp;

import android.app.Activity;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DownloadMonitorActivity extends AppCompatActivity {

    private Activity activity;
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
}
