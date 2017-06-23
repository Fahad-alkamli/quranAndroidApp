package alkamli.fahad.quranapp.quranapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.util.Log.e;

public class PlayerActivity extends AppCompatActivity {

    static TextView titleTextview;
    static Button playButton;
    static ProgressBar progressBar;
    static View progressBarContainer;
    private final String TAG="Alkamli";
    private static MediaPlayer mPlayer=null;
    private  static final int FAST_FORWARD_TIME=3000;
    private static boolean playerIsVisiable=true;
    private static String order;
    private boolean stop=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e(TAG,"OnCreate");
       String title= getIntent().getStringExtra("title");
        order=getIntent().getStringExtra("order");
        if(title==null || order==null)
        {
            finish();
            return;
        }
        order=order+".mp3";
        setContentView(R.layout.activity_player);

        //show the content and disable the progress bar
        progressBarContainer=findViewById(R.id.progressBarContainer);
        progressBarContainer.setVisibility(View.GONE);
        progressBar=(ProgressBar)  findViewById(R.id.progressBar);
        titleTextview=(TextView) findViewById(R.id.title);
        titleTextview.setText(title);

        String size=CommonFunctions.getSharedPreferences(getApplicationContext()).getString("titlesSize",null);

        if( size!= null && size.equals(CommonFunctions.textSizes.defaultSize.name())==false)
        {
            Log.d(TAG,"Size is : "+size);
            if(CommonFunctions.textSizes.smallSize.name().equals(size))
            {
                Log.d(TAG,"player title size has been set to 18ps");
                titleTextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            }
            if(CommonFunctions.textSizes.largeSize.name().equals(size))
            {
                Log.d(TAG,"player title size has been set to 30ps");
                titleTextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);;

            }

        }
        playButton=(Button) findViewById(R.id.playButton);
        playerIsVisiable=true;
        //Play the sound on the first open
        play(order);

    }

    @Override
    protected void onStart() {
        super.onStart();
        playerIsVisiable=true;
    }


    private void play(final String order2)
    {
        //play
        try {
        //If the file doesn't exists start downloading the file from the server and play it as soon as possible
        File file= new File(getApplicationInfo().dataDir+"/"+order2);
       // Log.e(TAG,getApplicationInfo().dataDir+"/"+order+".mp3");
        if(!file.exists() && CommonFunctions.getQueue().contains(order2)==false)
        {
            playButton.setEnabled(false);
         //   Toast.makeText(getApplicationContext(),R.string.file_does_not_exists,Toast.LENGTH_SHORT).show();
            new Thread(new Runnable(){
                @Override
                public void run()
                {
                   // Log.e(TAG,"File doesn't exists start downloading the file");
                    //Download the file
                    downloadFile(order2);
                }
            }).start();

            return;
        }
            //If file is being downloaded
           if( CommonFunctions.getQueue().contains(order2))
            {
                Toast.makeText(this, R.string.please_wait_for_file_to_finish_downloading,Toast.LENGTH_SHORT).show();
                return;
            }

            //Now we make sure the file being viewed is the same as the file finished downloading or something similar
            if(!order2.equals(order))
            {
                return;
            }

           if (mPlayer ==null)
            {
                mPlayer = new MediaPlayer();
                //http://stackoverflow.com/questions/18255458/trigger-an-event-when-mediaplayer-stops
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    stop(null);
                }
            });
                try {
                    mPlayer.setDataSource(getApplicationInfo().dataDir+"/"+order2);
                    mPlayer.prepare();
                    mPlayer.start();
                    Log.d(TAG,"playing .");
                } catch (IOException e) {
                     Log.e(TAG, "prepare() failed");
                }
            }else{
               mPlayer.start();
           }
        }catch(Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
        //Toast.makeText(getApplicationContext(),"Playing",Toast.LENGTH_SHORT).show();
        playButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_pause_black_24dp));
        playButton.setTag(true);
    }

    private void pause()
    {
        try {
            //Pause because it's already playing
            //Change the icon
            if(mPlayer!= null && mPlayer.isPlaying())
            {
                mPlayer.pause();
            }
            playButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_play_arrow_black_24dp));
            playButton.setTag(false);
        } catch (Exception e) {
            Log.d(TAG,e.getMessage());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        playerIsVisiable=false;
    }


    public void playOrPause(View view)
    {
        try {
            if(playButton.getTag()!= null && ((Boolean)playButton.getTag()))
            {
                pause();
            }else{
                play(order);
            }
        } catch (Exception e) {
            class Local {
            }
            ;
            e(CommonFunctions.TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));
        }
    }

    public void stop(View view)
    {
        try{
            if(mPlayer!=null )
            {
                mPlayer.stop();
                playButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_play_arrow_black_24dp));
                playButton.setTag(false);
                mPlayer.release();
                mPlayer=null;
            }

        } catch (Exception e) {
            class Local {
            }
            ;
            e(CommonFunctions.TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));
        }
    }

    public void fastforward(View view)
    {

        try {
            if(mPlayer!= null)
            {
                if(mPlayer.getDuration()>mPlayer.getCurrentPosition()+FAST_FORWARD_TIME)
                {
                    mPlayer.seekTo(mPlayer.getCurrentPosition()+FAST_FORWARD_TIME);
                }else
                {
                    // go the end
                    mPlayer.seekTo(mPlayer.getDuration());
                }
            }
        } catch (Exception e) {
            Log.d(TAG,e.getMessage());
        }
    }

    public void rewind(View view)
    {
        try {
            if(mPlayer!= null)
            {
                if(mPlayer.getDuration()>mPlayer.getCurrentPosition()-FAST_FORWARD_TIME)
                {
                    mPlayer.seekTo(mPlayer.getCurrentPosition()-FAST_FORWARD_TIME);
                }else{
                    //rewind From the start
                    mPlayer.seekTo(0);
                }

            }
        } catch (Exception e) {
            Log.d(TAG,e.getMessage());
        }

    }
    private void downloadFile(final String file)
    {
        //http://server6.mp3quran.net/thubti/001.mp3
        int count;
        try {
            stop=false;
            //Check for available internet connection first
            if(!CommonFunctions.isNetworkAvailable(getApplicationContext()))
            {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),R.string.internet_connection_error,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            //put the file in the queue
            CommonFunctions.putInQueue(file,this);
            //let's check for internet connection first

            //Hide the content and show the progress bar
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    findViewById(R.id.progressBarContainer).setVisibility(View.VISIBLE);
                }
            });
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
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.not_enough_space,Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                    return;
                }
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        progressBar.setMax(lenghtOfFile);
                    }
                });
            }
            InputStream input = new BufferedInputStream(url1.openStream());
            FileOutputStream output = new FileOutputStream(getApplicationInfo().dataDir+"/"+file);
            java.nio.channels.FileLock lock = output.getChannel().lock();
            byte data[] = new byte[1024];
            long total = 0;
            System.out.println("downloading.............");

           // Log.e(TAG,"File Total length: "+lenghtOfFile);
            while (CommonFunctions.isNetworkAvailable(getApplicationContext()) && (count = input.read(data)) != -1)
            {
                total += count;
                output.write(data, 0, count);
                final long  temp=total;
                if(stop)
                {
                    //stop the download and remove the file from the queue and delete it
                    //remove it from the queue first
                    CommonFunctions.removeFromQueue(file,this);
                    File file2=new File(getApplicationInfo().dataDir+"/"+file);
                    if(file2.exists())
                    {
                        try{
                            file2.delete();
                        }catch(Exception e)
                        {
                            Log.e(TAG,e.getMessage());
                        }

                    }
                    finish();
                    return;
                }
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        if(progressBar != null && file.equals(order))
                        {
                            progressBarContainer.setVisibility(View.VISIBLE);
                            progressBar.setMax(lenghtOfFile);
                            progressBar.setProgress(((int) temp));

                        }

                    }
                });
                //Log.d(TAG,Long.toString(temp));
            }
           //Log.e(TAG,"Done");
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
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.file_is_corrupt,Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();

                    return;
                }
            }

            //remove it from the queue first
            CommonFunctions.removeFromQueue(file,this);

            //Next play the sound but before that make sure that the user is still on the screen and didn't close it
            runOnUiThread(new Runnable(){
                @Override
                public void run()
                {
                    Toast.makeText(getApplicationContext(),R.string.download_finish,Toast.LENGTH_SHORT).show();
                    if(playerIsVisiable)
                    {
                        //Log.e(TAG,"playerIsVisiable");

                        play(file);
                    }
                }
            });
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
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.file_is_corrupt,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e1) {
                Log.e(TAG,e1.getMessage());
            }
//            Log.e(TAG,e.getMessage());

            e.printStackTrace();
            finish();
            //What if there was a problem how are we going to handle this?
        }
        //let's enable the button first
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                playButton.setEnabled(true);
                //show the content and disable the progress bar
                progressBarContainer.setVisibility(View.GONE);
            }
        });

        //Done

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.player_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.deleteSourah:
            {
                try{

                    //Don't forget to make sure that the file is not being downloaded
                    if(order != null&& CommonFunctions.getQueue().contains(order)==false  )
                    {
                        //Delete the file and exit the activity
                        File file2=new File(getApplicationInfo().dataDir+"/"+order);
                        if(file2.exists())
                        {
                            stop(null);
                            file2.delete();
                            Intent i=new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }catch(Exception e)
                {
                    Log.e(TAG,e.getMessage());
                }

                break;
            }
            case R.id.cancelDownload:
            {
                stop=true;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"Player Activity on destroy");
        try {
            playerIsVisiable=false;
            if(mPlayer!=null)
            {
                mPlayer.stop();
                mPlayer.release();
                mPlayer=null;
            }
        } catch (Exception e) {
            class Local {
            }
            ;
            e(CommonFunctions.TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //Log.e(TAG, "back button pressed");
            try {
                playerIsVisiable=false;
                if(mPlayer!=null)
                {
                    stop(null);
                }
            } catch (Exception e) {
                class Local {
                }
                ;
                e(CommonFunctions.TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
