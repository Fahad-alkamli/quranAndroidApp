package alkamli.fahad.quranapp.quranapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import alkamli.fahad.quranapp.quranapp.CommonFunctions;
import alkamli.fahad.quranapp.quranapp.DownloadAllQuranActivity;
import alkamli.fahad.quranapp.quranapp.PlayerActivity;
import alkamli.fahad.quranapp.quranapp.PlayerActivity2;
import alkamli.fahad.quranapp.quranapp.R;
import alkamli.fahad.quranapp.quranapp.entity.SurahItem;

import static alkamli.fahad.quranapp.quranapp.CommonFunctions.TAG;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.getFreeSpace;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.removeFromQueue;
import static alkamli.fahad.quranapp.quranapp.CommonFunctions.validateFileSize;
import static alkamli.fahad.quranapp.quranapp.R.string;
import static alkamli.fahad.quranapp.quranapp.R.string.file_is_corrupt;
import static android.util.Log.e;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class DownloadAllAdapter extends  RecyclerView.Adapter<DownloadAllAdapter.MyViewHolder>{

    private ArrayList<SurahItem> SourahList;
    public static boolean StopALL=false;
    private Activity activity;
    public  ArrayList<MyViewHolder> allHolders=new ArrayList<MyViewHolder>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
       public ProgressBar progressBar;
        public View view;

        public MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.title);

            String size=CommonFunctions.getSharedPreferences(activity).getString("titlesSize",null);
            if( size!= null && size.equals(CommonFunctions.textSizes.defaultSize.name())==false)
            {
                if(CommonFunctions.textSizes.smallSize.name().equals(size))
                {
                    title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                }
                if(CommonFunctions.textSizes.largeSize.name().equals(size))
                {
                    title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);;
                }

            }
            progressBar=(ProgressBar)  view.findViewById(R.id.progressBar);
            this.view=view;
        }
    }


    public DownloadAllAdapter(Activity activity,ArrayList<SurahItem> SourahList) {
        this.SourahList=SourahList;
        this.activity=activity;
        StopALL=false;
    }

    @Override
    public DownloadAllAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View CustomView = inflater.inflate(R.layout.download_item, parent, false);
        return new MyViewHolder(CustomView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if(this.SourahList==null)
        {
            return;
        }
        Log.d(TAG,""+position);
        String sourahTitle=SourahList.get(position).getTitle();

        final SurahItem item=SourahList.get(position);
        final String order=SourahList.get(position).getOrder();
        holder.title.setText(sourahTitle);
        holder.view.setTag(SourahList.get(position));
        holder.view.setClickable(true);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Show an option to pause or cancel the download
                //change the view tag state
                if(holder.progressBar.getTag()!= null &&((Boolean)holder.progressBar.getTag())==true)
                {
                    return;
                }
                PopupMenu popupMenu = new PopupMenu(activity, holder.title);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                         @Override
                                                         public boolean onMenuItemClick(MenuItem menuItem) {
                                                             switch (menuItem.getItemId())
                                                             {
                                                                 case R.id.cancelDownload:
                                                                     {
                                                                         SurahItem file=(SurahItem) view.getTag();
                                                                             file.setDownloadState(false);
                                                                     }
                                                             }
                                                             return false;
                                                         }
                                                     });

                popupMenu.inflate(R.menu.cancel_download_popup_menu);
                popupMenu.show();
            }
        });

        //we start the download here but we need to keep track the state. meaning if the user clicked paused we need that function to pause
        //
      /*  new Thread(new Runnable(){
            @Override
            public void run() {

                downloadFile(item,holder.progressBar);
            }
        }).start();*/

        allHolders.add(holder);

    }
    @Override
    public int getItemCount() {
        return SourahList.size();
    }



    private void downloadFile(SurahItem item,final ProgressBar progressBar)
    {

        //http://server6.mp3quran.net/thubti/001.mp3
        final String file=item.getOrder()+".mp3";
        int count;
        try {
            //Check for available internet connection first
            if (!CommonFunctions.isNetworkAvailable(activity)) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getApplicationContext(), R.string.internet_connection_error, Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                });
            }

            //let's check for internet connection first

            //Hide the content and show the progress bar
            if(CommonFunctions.getQueue().contains(file))
            {
                //file is being downloaded by other process
                Log.d(TAG,"file is being downloaded by other process:"+file);
                return;
            }
            {
                File file2 = new File(activity.getApplicationInfo().dataDir + "/" + file);
                // Log.e(TAG,getApplicationInfo().dataDir+"/"+order+".mp3");
                //The file already exists then we should change the progress to 100% , exists but not in the queue
                if (file2.exists())
                {
                    if (progressBar != null)
                    {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                                progressBar.setMax(100);
                                progressBar.setProgress(100);
                                progressBar.setTag(true);
                        }
                    });
                    }
                    return;
                }
            }
            Looper.prepare();
            URL url1 = new URL(CommonFunctions.getUsableURL(activity) + file);
            //  Log.e(TAG,"http://server6.mp3quran.net/thubti/"+file);
            URLConnection conexion = url1.openConnection();
            conexion.connect();
            final int lenghtOfFile = conexion.getContentLength();
            //Make sure we have enough space to download the file otherwise notify the user and cancel the download
            if (lenghtOfFile != -1 && lenghtOfFile > 0)
            {
                if (getFreeSpace() < lenghtOfFile) {
                    //Notify the user that the free space is not enough for the file
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), R.string.not_enough_space, Toast.LENGTH_LONG).show();
                           // activity.finish();

                        }
                    });
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setMax(lenghtOfFile);
                    }
                });
            }
            //put the file in the queue so we can start the download process
            CommonFunctions.putInQueue(file,activity);
            InputStream input = new BufferedInputStream(url1.openStream());
            FileOutputStream output = new FileOutputStream(activity.getApplicationInfo().dataDir + "/" + file);
            java.nio.channels.FileLock lock = output.getChannel().lock();
            byte data[] = new byte[1024];
            long total = 0;
            System.out.println("downloading.............");

            //Log.e(TAG, "File Total length: " + lenghtOfFile);
            //We initiate the progress bar tag by false indicating that this file has not finished yet
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setTag(false);

                }
            });
            while (CommonFunctions.isNetworkAvailable(activity.getApplicationContext()) && (count = input.read(data)) != -1)
            {
                total += count;
                output.write(data, 0, count);
                final long temp = total;
                if (progressBar != null)
                {
                    if((progressBar.getProgress()*3)<((int) temp))
                    {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(((int) temp));

                            }
                        });
                    }
                }
                //Log.d(TAG,Long.toString(temp));
                //Here we will detect if the user has clicked on pause on the download

                //we cancel the download if the cancel all button has been clicked or the user has canceled a download .
                if(!item.isDownloadState() || StopALL)
                {
                    Log.d(TAG,"Download has been Canceled");
                try{
                    {
                        File file2 = new File(activity.getApplicationInfo().dataDir + "/" + file);
                        if (file2.exists())
                        {
                            file2.delete();
                        }
                        CommonFunctions.removeFromQueue(file,activity);
                        progressBar.setTag(true);
                        return;
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
            if (lenghtOfFile != -1 && !validateFileSize(lenghtOfFile, file,activity))
            {
                Log.e(TAG, "File is corrupt , deleting the file, length does not match");
                //Delete the file
                File file2 = new File(activity.getApplicationInfo().dataDir + "/" + file);
                if (file2.exists())
                {
                    file2.delete();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), R.string.file_is_corrupt, Toast.LENGTH_SHORT).show();
                        }
                    });
                   // activity.finish();
                }
                //remove it from the queue first
                CommonFunctions.removeFromQueue(file,activity);
            } else {
                Log.d(TAG, "File has been downloaded successfully");
                CommonFunctions.removeFromQueue(file,activity);
                //We declare this download as completed
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setMax(100);
                        progressBar.incrementProgressBy(100);
                        progressBar.setTag(true);

                    }
                });

            }


        }catch(Exception e) {
            class Local {
            }
            ;
            e(TAG, ("MethodName: " + Local.class.getEnclosingMethod().getName() + " || ErrorMessage: " + e.getMessage()));
            e(TAG, "File is corrupt , deleting the file, some problem");

            //Delete the file
            File file2 = new File(activity.getApplicationInfo().dataDir + "/" + file);
            if (file2.exists()) {
                file2.delete();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeText(activity.getApplicationContext(), file_is_corrupt, LENGTH_SHORT).show();
                    }
                });
                //activity.finish();
            }
            //remove it from the queue first
            removeFromQueue(file,activity);
        }

    }



}


