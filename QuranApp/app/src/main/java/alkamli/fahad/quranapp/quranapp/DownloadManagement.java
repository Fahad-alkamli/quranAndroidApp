package alkamli.fahad.quranapp.quranapp;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Stack;

import alkamli.fahad.quranapp.quranapp.entity.SurahItem;

import static alkamli.fahad.quranapp.quranapp.CommonFunctions.TAG;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.getFreeSpace;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.getQueue;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.removeFromQueue;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.validateFileSize;
import static alkamli.fahad.quranapp.quranapp.R.string.file_is_corrupt;
import static android.util.Log.e;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class DownloadManagement {

    //We need to do the following
    /*
    1- run this for all the downloads this manager will start a download and then will monitor the state
    2-the download will put the file in the queue before downloading and will wait until it finishes
    3- when a file finish downloading we check it's outcome , was it a successful download or a failed one .
    4- in case it was a failed download we retry to download the file again.
    5- if the file finished downloading and is successful then we remove it from the queue and get the second in the queue
    6- keep in mind that the queue should be kept offline in case of a restart and should continue afterwords.
     */
    enum State{
        Downloaded,
        Canceled,
        NoConnection,
        FileExists,
        NoSpace,
        Corrupt
    }

    public static boolean active=false;

    static  Activity activity;
    ArrayList<SurahItem> allSourahs;
    public DownloadManagement(ArrayList<SurahItem> allSourahs,Activity activity)
    {
        if(active)
        {
            updateActivity(activity);
            return;

        }else{
            active=true;
            updateActivity(activity);
        }

        this.allSourahs=allSourahs;
        try{

            Stack<SurahItem> tempStack=new Stack<SurahItem>();
            tempStack.addAll(allSourahs);
           while(tempStack.size()!= 0)
           {
               SurahItem tempSurah= tempStack.firstElement();
               tempStack.remove(tempSurah);
                String file=tempSurah.getOrder()+".mp3";
               if(CommonFunctions.getQueue().contains(file))
               {
                   //Don't do anyTHING the file is being handled by another downloading function
                   continue;
               }
               CommonFunctions.putInQueue(file,getActivity());
               State currentState=downloadFile(tempSurah);
               if(currentState==State.Downloaded || currentState==State.FileExists)
               {
                   Log.d(TAG,file+" Download is complete");
                   //DownloadAllAdapter.MyViewHolder temp= this.holders.get(counter);
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                        //   temp.progressBar.
                       }
                   });

               }else if(currentState==State.NoConnection)
               {
                   //Cancel the while thing because we don't have internet connection
                   removeFromQueue(file,getActivity());
                   active=false;
                   return;
               }else if(currentState==State.Corrupt)
               {
                   //try again
                   downloadFile(tempSurah);
                   //remove from the queue , to give other files a chance

               }else if(currentState==State.Canceled || currentState==State.NoSpace)
               {
                   //Cancel all
                   CommonFunctions.removeFromQueue(file,getActivity());
                   Intent i=new Intent(getActivity(),HomeActivity.class);
                   getActivity().startActivity(i);
                   getActivity().finish();
                   active=false;
                   return;
               }else{
                   Log.e(TAG,"This is wrong!");

               }
               removeFromQueue(file,getActivity());
               Log.d(TAG,"download manager working");
           }

        }catch(Exception e)
        {
            Log.e(TAG,"download manager error: "+e.getMessage());
        }
        Log.d(TAG,"download manager finished");
        active=false;
    }


    private static synchronized Activity getActivity()
    {

        return activity;
    }
    private static synchronized void updateActivity(Activity activity1)
    {
        activity=activity1;
    }


    private State downloadFile(SurahItem item)
    {
        //http://server6.mp3quran.net/thubti/001.mp3
        final String file=item.getOrder()+".mp3";
        int count;
        try {
            //Check for available internet connection first
            if (!CommonFunctions.isNetworkAvailable(getActivity()))
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), R.string.internet_connection_error, Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
                return State.NoConnection;
            }

            //Hide the content and show the progress bar
            File file2 = new File(getActivity().getApplicationInfo().dataDir + "/" + file);
            if( file2.exists())
            {
                //file is being downloaded by other process
                Log.d(TAG,"file Exists already:"+file);
                return State.FileExists;
            }

            URL url1 = new URL(CommonFunctions.getUsableURL(getActivity()) + file);
            //  Log.e(TAG,"http://server6.mp3quran.net/thubti/"+file);
            URLConnection conexion = url1.openConnection();
            conexion.connect();
            final int lenghtOfFile = conexion.getContentLength();
            //Make sure we have enough space to download the file otherwise notify the user and cancel the download
            if (lenghtOfFile != -1 && lenghtOfFile > 0)
            {
                if (getFreeSpace() < lenghtOfFile)
                {
                    //Notify the user that the free space is not enough for the file
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), R.string.not_enough_space, Toast.LENGTH_LONG).show();
                            // activity.finish();
                        }
                    });
                    return State.NoSpace;
                }


            }
            //put the file in the queue so we can start the download process
           // CommonFunctions.putInQueue(file,getActivity());
            InputStream input = new BufferedInputStream(url1.openStream());
            FileOutputStream output = new FileOutputStream(getActivity().getApplicationInfo().dataDir + "/" + file);
            java.nio.channels.FileLock lock = output.getChannel().lock();
            byte data[] = new byte[1024];
            long total = 0;
            System.out.println("downloading.............");

            //Log.e(TAG, "File Total length: " + lenghtOfFile);
            //We initiate the progress bar tag by false indicating that this file has not finished yet
            while (CommonFunctions.isNetworkAvailable(getActivity().getApplicationContext()) && (count = input.read(data)) != -1)
            {
                total += count;
                output.write(data, 0, count);
                final long temp = total;
                //initiate the title and the progress bar
                iniProgressBar(lenghtOfFile,item.getTitle());
                //Update the progressbar
                updateProgress(((int) temp));
                boolean stop=((DownloadMonitorActivity) getActivity()).stopAll;
                if(stop)
                {
                    Log.d(TAG,"Download has been Canceled");
                    try{
                        {
                            File file3 = new File(getActivity().getApplicationInfo().dataDir + "/" + file);
                            if (file3.exists())
                            {
                                file3.delete();
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast. makeText(getActivity(), R.string.download_has_been_canceled,Toast.LENGTH_SHORT).show();
                                }
                            });
                            return State.Canceled;
                        }

                    }catch(Exception e) {
                        class Local {
                        }
                        ;
                        e(TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));
                    }
                }

            }

            Log.d(TAG, "Done");
            output.flush();
            //Release the lock on the file so others can use it
            lock.release();
            output.close();
            input.close();

            //Here i will try to find the file and check its size to make sure it's not corrupt
            //I can check the size only if the server respond with the expected size this is a place for a bug
            if (lenghtOfFile != -1 && !validateFileSize(lenghtOfFile, file,getActivity()))
            {
                Log.e(TAG, "File is corrupt , deleting the file, length does not match");
                //Delete the file
                File file4 = new File(getActivity().getApplicationInfo().dataDir + "/" + file);
                if (file2.exists())
                {
                    file4.delete();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(), R.string.file_is_corrupt, Toast.LENGTH_SHORT).show();
                        }
                    });
                    // activity.finish();
                }
                //remove it from the queue first
                CommonFunctions.removeFromQueue(file,getActivity());
                return State.Corrupt;
            } else {
                Log.d(TAG, "File has been downloaded successfully");
                CommonFunctions.removeFromQueue(file,getActivity());
                //We declare this download as completed
                return State.Downloaded;
            }


        }catch(Exception e) {
            class Local {
            }
            ;
            e(TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));
            e(TAG, "File is corrupt , deleting the file, some problem");

            //Delete the file
            File file2 = new File(getActivity().getApplicationInfo().dataDir + "/" + file);
            if (file2.exists()) {
                file2.delete();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeText(getActivity().getApplicationContext(), file_is_corrupt, LENGTH_SHORT).show();
                    }
                });
                //activity.finish();
            }
            //remove it from the queue first
            removeFromQueue(file,getActivity());
            return State.Corrupt;
        }
    }


    private void updateProgress(final int progress)
    {
        try{

            if (getActivity()==null)
            {
                return;
            }
            ProgressBar progressBar=(ProgressBar) getActivity().findViewById(R.id.progressBar);
            if ((progressBar.getProgress()*1.3)<progress)
            {
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Log.d(TAG,"updateProgress called");
                        ProgressBar progressBar=(ProgressBar) getActivity().findViewById(R.id.progressBar);
                        progressBar.setProgress(progress);
                    }
                });

            }

        }catch(Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
    }

    private void iniProgressBar(final int size,final String titleText)
    {

        try{
            if (getActivity()==null)
            {
                return;
            }
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    TextView title=(TextView) getActivity().findViewById(R.id.title);
                    ProgressBar progressBar=(ProgressBar) getActivity().findViewById(R.id.progressBar);
                    title.setText(titleText);
                    progressBar.setMax(size);
                }
            });

        }catch(Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
    }



}



