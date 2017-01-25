package alkamli.fahad.quranapp.quranapp;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class PlayerActivity extends AppCompatActivity {

    TextView titleTextview;
    Button playButton;
    private final String TAG="Alkamli";
    MediaPlayer mPlayer=null;
    private  static final int FAST_FORWARD_TIME=3000;
    String order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       String title= getIntent().getStringExtra("title");
        order=getIntent().getStringExtra("order");
        if(title==null || order==null)
        {
            finish();
            return;
        }
        setContentView(R.layout.activity_player);
        titleTextview=(TextView) findViewById(R.id.title);
        titleTextview.setText(title);
        playButton=(Button) findViewById(R.id.playButton);
        //Play the sound on the first open
        play();
    }


    private void play()
    {
        //If the file doesn't exists start downloading the file from the server and play it as soon as possible
        File file= new File(getApplicationInfo().dataDir+"/"+order+".mp3");
       // Log.e(TAG,getApplicationInfo().dataDir+"/"+order+".mp3");
        if(!file.exists())
        {
            Toast.makeText(getApplicationContext(),"File doesn't exists start downloading the file",Toast.LENGTH_LONG).show();
            new Thread(new Runnable(){
                @Override
                public void run()
                {
                   // Log.e(TAG,"File doesn't exists start downloading the file");
                    //Download the file
                    downloadFile(order+".mp3");
                }
            }).start();

            return;
        }
        //play
        try {
           if (mPlayer ==null)
            {
                mPlayer = new MediaPlayer();
                //http://stackoverflow.com/questions/18255458/trigger-an-event-when-mediaplayer-stops
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    //put the play icon since the player finished playing
                    playButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_play_arrow_black_24dp));
                    playButton.setTag(false);
                }
            });
                try {
                    mPlayer.setDataSource(getApplicationInfo().dataDir+"/"+order+".mp3");
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
        //Toast.makeText(getApplicationContext(),"Playing",Toast.LENGTH_LONG).show();
        playButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_pause_black_24dp));
        playButton.setTag(true);
    }

    private void pause()
    {
        //Pause because it's already playing
        //Change the icon
        if(mPlayer!= null && mPlayer.isPlaying())
        {
            mPlayer.pause();
        }
        playButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_play_arrow_black_24dp));
        playButton.setTag(false);
    }

    public void playOrPause(View view)
    {
        if(((Boolean)playButton.getTag()))
        {
            pause();
        }else{
            play();
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
            Log.e(TAG,e.getMessage());
        }
    }
    public void fastforward(View view)
    {

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
    }
    public void rewind(View view)
    {
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

    }
    private void downloadFile(String file)
    {
        //http://server6.mp3quran.net/thubti/001.mp3
        int count;
        try {
            Looper.prepare();
            URL url1 = new URL("http://server6.mp3quran.net/thubti/"+file);
          //  Log.e(TAG,"http://server6.mp3quran.net/thubti/"+file);
            URLConnection conexion = url1.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            InputStream input = new BufferedInputStream(url1.openStream());
            OutputStream output = new FileOutputStream(getApplicationInfo().dataDir+"/"+file);
            byte data[] = new byte[1024];
            long total = 0;
            System.out.println("downloading.............");
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            Toast.makeText(getApplicationContext(),"Download finished",Toast.LENGTH_LONG).show();
            //Next play the sound
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    play();
                }
            });
        } catch (Exception e)
        {
                Log.e(TAG,e.getMessage());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(mPlayer!=null)
            {
                mPlayer.stop();
                mPlayer.release();
                mPlayer=null;
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }
}
