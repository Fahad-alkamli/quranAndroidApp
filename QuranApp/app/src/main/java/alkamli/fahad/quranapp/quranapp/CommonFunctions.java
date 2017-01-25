package alkamli.fahad.quranapp.quranapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import alkamli.fahad.quranapp.quranapp.entity.SurahItem;

import static android.content.Context.MODE_PRIVATE;


public class CommonFunctions {


    public static final String TAG="Alkamli";

    public static ArrayList<SurahItem> getSourahList(Context context)
    {
        ArrayList<SurahItem> list=new ArrayList<>();
        list.add(new SurahItem(context.getString(R.string.Al_Fatihah),"001"));
        list.add(new SurahItem(context.getString(R.string.Al_Baqarah),"002"));
       // Log.e(TAG,Integer.toString(list.size()));
        return list;
    }


    public static SharedPreferences.Editor getEditor(Context context)
    {

        SharedPreferences.Editor editor=context.getApplicationContext().getSharedPreferences(context.getString(R.string.app_name),MODE_PRIVATE).edit();

        return editor;
    }

    public static SharedPreferences getSharedPreferences(Context context)
    {

        return context.getSharedPreferences(context.getString(R.string.app_name),MODE_PRIVATE);
    }

}
