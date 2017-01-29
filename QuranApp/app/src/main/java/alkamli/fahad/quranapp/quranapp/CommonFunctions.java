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
        list.add(new SurahItem(context.getString(R.string.Al_Imran),"003"));
        list.add(new SurahItem(context.getString(R.string.An_Nisa),"004"));
        list.add(new SurahItem(context.getString(R.string.Al_Maidah),"005"));
        //New
        list.add(new SurahItem("Al-An'am","006"));
        list.add(new SurahItem("Al-A'raf","007"));
        list.add(new SurahItem("Al-Anfal","008"));
        list.add(new SurahItem("At-Taubah","009"));
        list.add(new SurahItem("Yunus","010"));
        list.add(new SurahItem("Hood","011"));
        list.add(new SurahItem("Yusuf","012"));
        list.add(new SurahItem("Ar-Ra'd","013"));
        list.add(new SurahItem("Ibrahim","014"));
        list.add(new SurahItem("Al-Hijr","015"));
        list.add(new SurahItem("An-Nahl","016"));
        list.add(new SurahItem("Al-Isra","017"));
        list.add(new SurahItem("Al-Kahf","018"));
        list.add(new SurahItem("Maryam","019"));
        list.add(new SurahItem("Taha","020"));
        list.add(new SurahItem("Al-Anbiya'","021"));
        list.add(new SurahItem("Al-Hajj","022"));
        list.add(new SurahItem("Al-Mu'minun","023"));
        list.add(new SurahItem("An-Nur","024"));
        list.add(new SurahItem("Al-Furqan","025"));
        list.add(new SurahItem("Ash-Shu'ara'","026"));
        list.add(new SurahItem("An-Naml","027"));
        list.add(new SurahItem("Al-Qasas","028"));
        list.add(new SurahItem("Al-'Ankabut","029"));
        list.add(new SurahItem("ArRoom","030"));
        list.add(new SurahItem("Luqman","031"));
        list.add(new SurahItem("AsSajdah","032"));
        list.add(new SurahItem("AlAhzab","033"));
        list.add(new SurahItem("Saba'","034"));
        list.add(new SurahItem("Fatir","035"));
        list.add(new SurahItem("YaSin","036"));
        list.add(new SurahItem("As-Saffat","037"));
        list.add(new SurahItem("Sad","038"));
        list.add(new SurahItem("Az-Zumar","039"));
        list.add(new SurahItem("Ghafir","040"));
       // Log.e(TAG,Integer.toString(list.size()));
        list.add(new SurahItem("Fussilat","041"));
        list.add(new SurahItem("Ash-Shura","042"));
        list.add(new SurahItem("Az-Zukhruf","043"));
        list.add(new SurahItem("Ad-Dukhan","044"));
        list.add(new SurahItem("Al-Jathiya","045"));
        list.add(new SurahItem("Al-Ahqaf","046"));
        list.add(new SurahItem("Muhammad","047"));
        list.add(new SurahItem("Al-Fath","048"));
        list.add(new SurahItem("Al-Hujurat","049"));
        list.add(new SurahItem("Qaf","050"));
        //Line
        list.add(new SurahItem("Az-Zariyat","051"));
        list.add(new SurahItem("At-Tur","052"));
        list.add(new SurahItem("An-Najm","053"));
        list.add(new SurahItem("Al-Qamar","054"));
        list.add(new SurahItem("Ar-Rahman","055"));
        list.add(new SurahItem("Al-Waqi'ah","056"));
        list.add(new SurahItem("Al-Hadid","057"));
        list.add(new SurahItem("Al-Mujadilah","058"));
        list.add(new SurahItem("Al-Hashr","059"));
        list.add(new SurahItem("Al-Mumtahinah","060"));
        list.add(new SurahItem("As-Saff","061"));
        list.add(new SurahItem("Al-Jumu'ah","062"));
        list.add(new SurahItem("Al-Munafiqun","063"));
        list.add(new SurahItem("At-Taghabun","064"));
        list.add(new SurahItem("At-Talaq","065"));
        list.add(new SurahItem("At-Tahrim","066"));
        list.add(new SurahItem("Al-Mulk","067"));
        list.add(new SurahItem("Al-Qalam","068"));
        list.add(new SurahItem("Al-Haqqah","069"));
        list.add(new SurahItem("Al-Ma'arij","070"));
        //Line
        list.add(new SurahItem("Nooh","071"));
        list.add(new SurahItem("Al-Jinn","072"));
        list.add(new SurahItem("Al-Muzzammil","073"));
        list.add(new SurahItem("Al-Muddaththir","074"));
        list.add(new SurahItem("Al-Qiyamah","075"));
        list.add(new SurahItem("Al-Insan","076"));
        list.add(new SurahItem("Al-Mursalat","077"));
        list.add(new SurahItem("An-Naba'","078"));
        list.add(new SurahItem("An-Nazi'at","079"));
        list.add(new SurahItem("'Abasa","080"));
        list.add(new SurahItem("At-Takwir","081"));
        list.add(new SurahItem("Al-Infitar","082"));
        list.add(new SurahItem("Al-Mutaffifin","083"));
        list.add(new SurahItem("Al-Inshiqaq","084"));
        list.add(new SurahItem("Al-Buruj","085"));
        list.add(new SurahItem("At-Tariq","086"));
        list.add(new SurahItem("Al-A'la","087"));
        list.add(new SurahItem("Al-Ghashiyah","088"));
        list.add(new SurahItem("Al-Fajr","089"));
        list.add(new SurahItem("Al-Balad","090"));
        list.add(new SurahItem("Ash-Shams","091"));
        list.add(new SurahItem("Al-Lail","092"));
        list.add(new SurahItem("Ad-Duha","093"));
        list.add(new SurahItem("Ash-Sharh","094"));
        list.add(new SurahItem("At-Tin","095"));
        list.add(new SurahItem("Al-'Alaq","096"));
        list.add(new SurahItem("Al-Qadr","097"));
        list.add(new SurahItem("Al-Baiyinah","098"));
        list.add(new SurahItem("Az-Zalzalah","099"));
        list.add(new SurahItem("Al-'Adiyat","100"));


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
