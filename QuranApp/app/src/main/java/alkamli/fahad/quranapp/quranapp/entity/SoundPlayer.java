package alkamli.fahad.quranapp.quranapp.entity;


import android.media.MediaPlayer;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import alkamli.fahad.quranapp.quranapp.CommonFunctions;
import alkamli.fahad.quranapp.quranapp.R;

public class SoundPlayer {
    private final String TAG="Alkamli";
    private MediaPlayer mPlayer=null;
    private  static final int FAST_FORWARD_TIME=3000;
    private String order;
    TextView titleTextview;
    Button playButton;
    ProgressBar progressBar;
    AppCompatActivity activity;
    boolean playerIsVisiable;
    public SoundPlayer(String order,TextView titleTextview,Button playButton,ProgressBar progressBar,boolean playerIsVisiable,AppCompatActivity activity)
    {
        this.titleTextview=titleTextview;
        this.playButton=playButton;
        this.progressBar=progressBar;
        this.activity=activity;
        this.playerIsVisiable=playerIsVisiable;
this.order=order;

    }
    public void play()
    {
        //play
        try {
            //If the file doesn't exists start downloading the file from the server and play it as soon as possible
            File file= new File(activity.getApplicationInfo().dataDir+"/"+order);
            // Log.e(TAG,getApplicationInfo().dataDir+"/"+order+".mp3");
            if(!file.exists())
            {
                playButton.setEnabled(false);
                //   Toast.makeText(getApplicationContext(),R.string.file_does_not_exists,Toast.LENGTH_SHORT).show();
                new Thread(new Runnable(){
                    @Override
                    public void run()
                    {
                        // Log.e(TAG,"File doesn't exists start downloading the file");
                        //Download the file
                        downloadFile(order);
                    }
                }).start();

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
                        stop();
                    }
                });
                try {
                    mPlayer.setDataSource(activity.getApplicationInfo().dataDir+"/"+order);
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
        setPauseIcon();
        playButton.setTag(true);
    }


    public void setPauseIcon()
    {
        playButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.mipmap.ic_pause_black_24dp));
    }
    public void pause()
    {
        try {
            //Pause because it's already playing
            //Change the icon
            if(mPlayer!= null && mPlayer.isPlaying())
            {
                mPlayer.pause();
            }
            playButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.mipmap.ic_play_arrow_black_24dp));
            playButton.setTag(false);
        } catch (Exception e) {
            Log.d(TAG,e.getMessage());
        }
    }


    public void downloadFile(String file)
    {
        //http://server6.mp3quran.net/thubti/001.mp3
        int count;
        try {
            //Check for available internet connection first
            if(!CommonFunctions.isNetworkAvailable(activity.getApplicationContext()))
            {
                activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Toast.makeText(activity.getApplicationContext(),R.string.internet_connection_error,Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                });
            }

            //let's check for internet connection first

            //Hide the content and show the progress bar
            activity.runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    activity.findViewById(R.id.progressBarContainer).setVisibility(View.VISIBLE);
                }
            });
            Looper.prepare();
            URL url1 = new URL(activity.getString(R.string.files_url)+file);
            //  Log.e(TAG,"http://server6.mp3quran.net/thubti/"+file);
            URLConnection conexion = url1.openConnection();
            conexion.connect();
            final int lenghtOfFile = conexion.getContentLength();
            if(lenghtOfFile!=-1 && lenghtOfFile>0)
            {
                activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        progressBar.setMax(lenghtOfFile);
                    }
                });
            }
            InputStream input = new BufferedInputStream(url1.openStream());
            OutputStream output = new FileOutputStream(activity.getApplicationInfo().dataDir+"/"+file);
            byte data[] = new byte[1024];
            long total = 0;
            System.out.println("downloading.............");
            Log.e(TAG,"File Total length: "+lenghtOfFile);
            while (CommonFunctions.isNetworkAvailable(activity.getApplicationContext()) && (count = input.read(data)) != -1)
            {
                total += count;
                output.write(data, 0, count);
                final long  temp=total;
                activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        progressBar.setProgress(((int)temp));
                    }
                });
            }
            Log.e(TAG,"Done");
            output.flush();
            output.close();
            input.close();
            //Here i will try to find the file and check it's size to make sure it's not corrupt
            //I can check the size only if the server respond with the expected size
            if(lenghtOfFile !=-1 && !validateFileSize(lenghtOfFile,file))
            {
                Log.e(TAG,"File is corrupt , deleting the file");
                //Delete the file
                File file2=new File(activity.getApplicationInfo().dataDir+"/"+file);
                if(file2.exists())
                {
                    file2.delete();
                    activity.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), R.string.file_is_corrupt,Toast.LENGTH_SHORT).show();
                        }
                    });
                    activity.finish();
                    return;
                }
            }
            Toast.makeText(activity.getApplicationContext(),R.string.download_finish,Toast.LENGTH_SHORT).show();
            //Next play the sound but before that make sure that the user is still on the screen and didn't close it
            activity.runOnUiThread(new Runnable(){
                @Override
                public void run()
                {
                    if(playerIsVisiable)
                    {
                        play();
                    }
                }
            });
        } catch (Exception e)
        {
            try {
                //If there was an error just delete the file
                //Delete the file
                File file2=new File(activity.getApplicationInfo().dataDir+"/"+file);
                if(file2.exists())
                {
                    file2.delete();
                    activity.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), R.string.file_is_corrupt,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e1) {
                Log.e(TAG,e1.getMessage());
            }
            Log.e(TAG,e.getMessage());

            activity.finish();
            //What if there was a problem how are we going to handle this?
        }
        //let's enable the button first
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                playButton.setEnabled(true);
                //show the content and disable the progress bar
                activity.findViewById(R.id.progressBarContainer).setVisibility(View.GONE);
            }
        });
    }


    public boolean validateFileSize(long expectedSize,String fileName)
    {
        try{
            //Here i will try to find the file and check it's size to make sure it's not corrupt
            File file2=new File(activity.getApplicationInfo().dataDir+"/"+fileName);
            if(file2.exists())
            {
                Log.e(TAG,"Written file length: "+file2.length());
                Log.e(TAG,"ExpectedSize to be:"+expectedSize);
                if(file2.length()==expectedSize)
                {
                    return true;
                }
            }


        }catch(Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
        return false;

    }


    public void stop()
    {
        try{
            if(mPlayer!=null )
            {
                mPlayer.stop();
                playButton.setBackground(ContextCompat.getDrawable(activity.getApplicationContext(), R.mipmap.ic_play_arrow_black_24dp));
                playButton.setTag(false);
                mPlayer.release();
                mPlayer=null;
            }

        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }


    public void fastforward()
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

    public void rewind()
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
}
