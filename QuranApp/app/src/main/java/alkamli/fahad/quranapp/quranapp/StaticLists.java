package alkamli.fahad.quranapp.quranapp;


import android.util.Log;

import java.util.ArrayList;

import alkamli.fahad.quranapp.quranapp.entity.SurahItem;


public class StaticLists {


    public static final String TAG="Alkamli";

    public static ArrayList<SurahItem> getSourahList()
    {
        ArrayList<SurahItem> list=new ArrayList<>();
        list.add(new SurahItem("Al-Fatihah","001"));
        list.add(new SurahItem("Al-Baqarah ","002"));
        Log.e(TAG,Integer.toString(list.size()));
        return list;
    }
}
