package alkamli.fahad.quranapp.quranapp;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Set;

import alkamli.fahad.quranapp.quranapp.adapter.HomeAdapter;

import static alkamli.fahad.quranapp.quranapp.CommonFunctions.TAG;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.getQueue;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.getSharedPreferences;

public class HomeActivity extends AppCompatActivity {

    static RecyclerView list;
    static Context context=null;
    static Thread oldFilesThread=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate HomeActivity");

        if(oldFilesThread==null)
        {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    checkForFilesWaitingInTheList();
                }
            }).start();
        }

        context=this;
        list=(RecyclerView) findViewById(R.id.list);
        list.setAdapter(new HomeAdapter(this, CommonFunctions.getSourahList(this)));
        list.setLayoutManager(new LinearLayoutManager(this));
        //Delete files if the option has been selected
        new Thread(new Runnable(){
            @Override
            public void run()
            {
                CommonFunctions.checkForDeleteFilesOption(getApplicationContext());
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

            inflater.inflate(R.menu.toolbar_menu, menu);
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
                case R.id.downloadAll:
                {
                    //Intent i=new Intent(getApplicationContext(),DownloadAllQuranActivity.class);
                    Intent i=new Intent(getApplicationContext(),DownloadMonitorActivity.class);
                    startActivity(i);
                    return true;
                }

            }
        }catch(Exception e)
        {
            class Local {};
            Log.e(CommonFunctions.TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));
        }


        return false;
    }


    public static void updateAdapter()
    {
        try{
            if(context != null && list != null)
            {
                list.setAdapter(new HomeAdapter(context, CommonFunctions.getSourahList(context)));
                list.setLayoutManager(new LinearLayoutManager(context));
            }

        }catch(Exception e)
        {
            Log.e(CommonFunctions.TAG,e.getMessage());
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable(){
            @Override
            public void run()
            {
                CommonFunctions.checkForDeleteFilesOption(getApplicationContext());
                Log.d(CommonFunctions.TAG,"HomeActivity onDestroy");
            }
        }).start();
    }


    private void checkForFilesWaitingInTheList()
    {
        try{
            if(CommonFunctions.getSharedPreferences(context).getBoolean("resumeDownload", false))
            {
                //clear the list
                getQueue().clear();
                CommonFunctions.getEditor(context).remove("queueList").commit();
                return;
            }
            Log.d(TAG,"checkForFilesWaitingInTheList");
            Set tempSet=CommonFunctions.getSharedPreferences(this).getStringSet("queueList",null);

            if(tempSet==null)
            {
                return;
            }
            ArrayList<String> tempArrayList=new ArrayList<String>();
            tempArrayList.addAll(tempSet);
            //delete the files in the queue if they exist and try to re-download them
            for(String temp:tempArrayList)
            {
                Log.d(TAG,"Old files may be corrupted found: "+temp);
                //Delete the file
                File file2=new File(getApplicationInfo().dataDir+"/"+(temp+".mp3"));
                if(file2.exists())
                {
                    file2.delete();
                }
                //Try to re download it
                downloadFile(temp);
            }

        }catch(Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
    }

    private void downloadFile(final String file)
    {
        //http://server6.mp3quran.net/thubti/001.mp3
        int count;
        try {
            //Check for available internet connection first
            if(!CommonFunctions.isNetworkAvailable(getApplicationContext()))
            {
                return;
            }
            //put the file in the queue
            CommonFunctions.putInQueue(file,this);
            //let's check for internet connection first

            Looper.prepare();
            URL url1 = new URL(CommonFunctions.getUsableURL(getApplicationContext())+file);
            //  Log.e(TAG,"http://server6.mp3quran.net/thubti/"+file);
            URLConnection conexion = url1.openConnection();
            conexion.connect();
            final int lenghtOfFile = conexion.getContentLength();
            if(lenghtOfFile!=-1 && lenghtOfFile>0)
            {
                if(CommonFunctions.getFreeSpace()<lenghtOfFile)
                {
                    //Notify the user that the free space is not enough for the file
                    return;
                }
            }
            InputStream input = new BufferedInputStream(url1.openStream());
            FileOutputStream output = new FileOutputStream(getApplicationInfo().dataDir+"/"+file);
            java.nio.channels.FileLock lock = output.getChannel().lock();
            byte data[] = new byte[1024];
            long total = 0;
            System.out.println("downloading.............");

            while (CommonFunctions.isNetworkAvailable(getApplicationContext()) && (count = input.read(data)) != -1)
            {
                total += count;
                output.write(data, 0, count);
                final long  temp=total;
            }
            output.flush();
            //Release the lock on the file so others can use it
            lock.release();
            output.close();
            input.close();

            //Here i will try to find the file and check it's size to make sure it's not corrupt
            //I can check the size only if the server respond with the expected size
            if(lenghtOfFile !=-1 && !CommonFunctions.validateFileSize(lenghtOfFile,file,getApplicationContext()))
            {
                Log.e(TAG,"File is corrupt , deleting the file");
                //remove it from the queue first
                CommonFunctions.removeFromQueue(file,this);
                //Delete the file
                File file2=new File(getApplicationInfo().dataDir+"/"+file);
                if(file2.exists())
                {
                    file2.delete();
                    return;
                }
            }
            //remove it from the queue first
            CommonFunctions.removeFromQueue(file,this);


        } catch (Exception e)
        {
            try {
                //If there was an error just delete the file
                //Delete the file
                //remove it from the queue first
                CommonFunctions.removeFromQueue(file,this);
                File file2=new File(getApplicationInfo().dataDir+"/"+file);
                if(file2.exists())
                {
                    file2.delete();
                }
            } catch (Exception e1) {
                Log.e(TAG,e1.getMessage());
            }
//            Log.e(TAG,e.getMessage());

            e.printStackTrace();
            //What if there was a problem how are we going to handle this?
        }
        //Done

    }


}
