package alkamli.fahad.quranapp.quranapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import alkamli.fahad.quranapp.quranapp.adapter.DownloadAllAdapter;
import alkamli.fahad.quranapp.quranapp.entity.SurahItem;


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
                    DownloadAllAdapter.StopALL=true;
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

}
