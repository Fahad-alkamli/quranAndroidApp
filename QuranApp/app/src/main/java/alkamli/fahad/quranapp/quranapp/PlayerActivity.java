package alkamli.fahad.quranapp.quranapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
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

public class PlayerActivity extends AppCompatActivity {

    TextView titleTextview;
    Button playButton;
    ProgressBar progressBar;
    private final String TAG="Alkamli";
    MediaPlayer mPlayer=null;
    private  static final int FAST_FORWARD_TIME=3000;
    boolean playerIsVisiable=true;
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
        order=order+".mp3";
        setContentView(R.layout.activity_player);
        //show the content and disable the progress bar
        findViewById(R.id.progressBarContainer).setVisibility(View.GONE);
        progressBar=(ProgressBar)  findViewById(R.id.progressBar);
        titleTextview=(TextView) findViewById(R.id.title);
        titleTextview.setText(title);
        playButton=(Button) findViewById(R.id.playButton);

        //Play the sound on the first open
        play();
    }


    private void play()
    {
        //play
        try {
        //If the file doesn't exists start downloading the file from the server and play it as soon as possible
        File file= new File(getApplicationInfo().dataDir+"/"+order);
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
                    stop(null);
                }
            });
                try {
                    mPlayer.setDataSource(getApplicationInfo().dataDir+"/"+order);
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

    public void playOrPause(View view)
    {
        try {
            if(playButton.getTag()!= null && ((Boolean)playButton.getTag()))
            {
                pause();
            }else{
                play();
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
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
    private void downloadFile(String file)
    {
        //http://server6.mp3quran.net/thubti/001.mp3
        int count;
        try {
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

            //let's check for internet connection first

            //Hide the content and show the progress bar
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    findViewById(R.id.progressBarContainer).setVisibility(View.VISIBLE);
                }
            });
            Looper.prepare();
            URL url1 = new URL(getString(R.string.files_url)+file);
          //  Log.e(TAG,"http://server6.mp3quran.net/thubti/"+file);
            URLConnection conexion = url1.openConnection();
            conexion.connect();
            final int lenghtOfFile = conexion.getContentLength();
            if(lenghtOfFile!=-1 && lenghtOfFile>0)
            {
                if(getFreeSpace()<lenghtOfFile)
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
            OutputStream output = new FileOutputStream(getApplicationInfo().dataDir+"/"+file);
            byte data[] = new byte[1024];
            long total = 0;
            System.out.println("downloading.............");
            Log.e(TAG,"File Total length: "+lenghtOfFile);
            while (CommonFunctions.isNetworkAvailable(getApplicationContext()) && (count = input.read(data)) != -1)
            {
                total += count;
                output.write(data, 0, count);
                final long  temp=total;
                runOnUiThread(new Runnable(){
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
            Toast.makeText(getApplicationContext(),R.string.download_finish,Toast.LENGTH_SHORT).show();
            //Next play the sound but before that make sure that the user is still on the screen and didn't close it
            runOnUiThread(new Runnable(){
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
            Log.e(TAG,e.getMessage());

            finish();
            //What if there was a problem how are we going to handle this?
        }
        //let's enable the button first
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                playButton.setEnabled(true);
                //show the content and disable the progress bar
                findViewById(R.id.progressBarContainer).setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        playerIsVisiable=false;
    }

    private boolean validateFileSize(long expectedSize,String fileName)
    {
        try{
            //Here i will try to find the file and check it's size to make sure it's not corrupt
            File file2=new File(getApplicationInfo().dataDir+"/"+fileName);
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


    /**
     * This function find outs the free space for the given path.
     *http://stackoverflow.com/questions/16076122/android-how-to-handle-saving-file-on-low-device-memoryinternal-external-memor
     * @return Bytes. Number of free space in bytes.
     */
    public static long getFreeSpace()
    {
        try
        {
            if (Environment.getExternalStorageDirectory() != null
                    && Environment.getExternalStorageDirectory().getPath() != null)
            {
                StatFs m_stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
                long m_blockSize = m_stat.getBlockSizeLong();
                long m_availableBlocks = m_stat.getAvailableBlocksLong();
                return (m_availableBlocks * m_blockSize);
            }
            else
            {
                return 0;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            playerIsVisiable=false;
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
