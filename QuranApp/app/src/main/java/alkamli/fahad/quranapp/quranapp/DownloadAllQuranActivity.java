package alkamli.fahad.quranapp.quranapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import alkamli.fahad.quranapp.quranapp.adapter.DownloadAllAdapter;


public class DownloadAllQuranActivity extends AppCompatActivity {

    static RecyclerView list;
    Context context;
    DownloadAllAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_all_quran);
        context=this;
        list=(RecyclerView) findViewById(R.id.list);
        adapter=new DownloadAllAdapter(this, CommonFunctions.getSourahList(this));
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(this));
       /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(new DownloadAllAdapter(this, CommonFunctions.getSourahList(this)));

        //list.setLayoutManager(new LinearLayoutManager(this));
        Log.d(CommonFunctions.TAG,"Setting the layout now");*/


    }

}
