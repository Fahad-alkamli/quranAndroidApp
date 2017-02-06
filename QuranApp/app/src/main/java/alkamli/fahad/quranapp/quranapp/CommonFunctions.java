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
        list.add(new SurahItem(context.getString(R.string.Al_Anam),"006"));
        list.add(new SurahItem(context.getString(R.string.Al_Araf),"007"));
        list.add(new SurahItem(context.getString(R.string.Al_Anfal),"008"));
        list.add(new SurahItem(context.getString(R.string.At_Taubah),"009"));
        list.add(new SurahItem(context.getString(R.string.Yunus),"010"));
        list.add(new SurahItem(context.getString(R.string.Hood),"011"));
        list.add(new SurahItem(context.getString(R.string.Yusuf),"012"));
        list.add(new SurahItem(context.getString(R.string.Ar_Rad),"013"));
        list.add(new SurahItem(context.getString(R.string.Ibrahim),"014"));
        list.add(new SurahItem(context.getString(R.string.Al_Hijr),"015"));
        list.add(new SurahItem(context.getString(R.string.An_Nahl),"016"));
        list.add(new SurahItem(context.getString(R.string.Al_Isra),"017"));
        list.add(new SurahItem(context.getString(R.string.Al_Kahf),"018"));
        list.add(new SurahItem(context.getString(R.string.Maryam),"019"));
        list.add(new SurahItem(context.getString(R.string.Taha),"020"));
        list.add(new SurahItem(context.getString(R.string.Al_Anbiya),"021"));
        list.add(new SurahItem(context.getString(R.string.Al_Hajj),"022"));
        list.add(new SurahItem(context.getString(R.string.Al_Muminun),"023"));
        list.add(new SurahItem(context.getString(R.string.An_Nur),"024"));
        list.add(new SurahItem(context.getString(R.string.Al_Furqan),"025"));
        list.add(new SurahItem(context.getString(R.string.Ash_Shuara),"026"));
        list.add(new SurahItem(context.getString(R.string.An_Naml),"027"));
        list.add(new SurahItem(context.getString(R.string.Al_Qasas),"028"));
        list.add(new SurahItem(context.getString(R.string.Al_Ankabut),"029"));
        list.add(new SurahItem(context.getString(R.string.ArRoom),"030"));
        list.add(new SurahItem(context.getString(R.string.Luqman),"031"));
        list.add(new SurahItem(context.getString(R.string.AsSajdah),"032"));
        list.add(new SurahItem(context.getString(R.string.AlAhzab),"033"));
        list.add(new SurahItem(context.getString(R.string.Saba),"034"));
        list.add(new SurahItem(context.getString(R.string.Fatir),"035"));
        list.add(new SurahItem(context.getString(R.string.YaSin),"036"));
        list.add(new SurahItem(context.getString(R.string.As_Saffat),"037"));
        list.add(new SurahItem(context.getString(R.string.Sad),"038"));
        list.add(new SurahItem(context.getString(R.string.Az_Zumar),"039"));
        list.add(new SurahItem(context.getString(R.string.Ghafir),"040"));
       // Log.e(TAG,Integer.toString(list.size()));
        list.add(new SurahItem(context.getString(R.string.Fussilat),"041"));
        list.add(new SurahItem(context.getString(R.string.Ash_Shura),"042"));
        list.add(new SurahItem(context.getString(R.string.Az_Zukhruf),"043"));
        list.add(new SurahItem(context.getString(R.string.Ad_Dukhan),"044"));
        list.add(new SurahItem(context.getString(R.string.Al_Jathiya),"045"));
        list.add(new SurahItem(context.getString(R.string.Al_Ahqaf),"046"));
        list.add(new SurahItem(context.getString(R.string.Muhammad),"047"));
        list.add(new SurahItem(context.getString(R.string.Al_Fath),"048"));
        list.add(new SurahItem(context.getString(R.string.Al_Hujurat),"049"));
        list.add(new SurahItem(context.getString(R.string.Qaf),"050"));
        //Line
        list.add(new SurahItem(context.getString(R.string.Az_Zariyat),"051"));
        list.add(new SurahItem(context.getString(R.string.At_Tur),"052"));
        list.add(new SurahItem(context.getString(R.string.An_Najm),"053"));
        list.add(new SurahItem(context.getString(R.string.Al_Qamar),"054"));
        list.add(new SurahItem(context.getString(R.string.Ar_Rahman),"055"));
        list.add(new SurahItem(context.getString(R.string.Al_Waqiah),"056"));
        list.add(new SurahItem(context.getString(R.string.Al_Hadid),"057"));
        list.add(new SurahItem(context.getString(R.string.Al_Mujadilah),"058"));
        list.add(new SurahItem(context.getString(R.string.Al_Hashr),"059"));
        list.add(new SurahItem(context.getString(R.string.Al_Mumtahinah),"060"));
        list.add(new SurahItem(context.getString(R.string.As_Saff),"061"));
        list.add(new SurahItem(context.getString(R.string.Al_Jumuah),"062"));
        list.add(new SurahItem(context.getString(R.string.Al_Munafiqun),"063"));
        list.add(new SurahItem(context.getString(R.string.At_Taghabun),"064"));
        list.add(new SurahItem(context.getString(R.string.At_Talaq),"065"));
        list.add(new SurahItem(context.getString(R.string.At_Tahrim),"066"));
        list.add(new SurahItem(context.getString(R.string.Al_Mulk),"067"));
        list.add(new SurahItem(context.getString(R.string.Al_Qalam),"068"));
        list.add(new SurahItem(context.getString(R.string.Al_Haqqah),"069"));
        list.add(new SurahItem(context.getString(R.string.Al_Maarij),"070"));
        //Line
        list.add(new SurahItem(context.getString(R.string.Nooh),"071"));
        list.add(new SurahItem(context.getString(R.string.Al_Jinn),"072"));
        list.add(new SurahItem(context.getString(R.string.Al_Muzzammil),"073"));
        list.add(new SurahItem(context.getString(R.string.Al_Muddaththir),"074"));
        list.add(new SurahItem(context.getString(R.string.Al_Qiyamah),"075"));
        list.add(new SurahItem(context.getString(R.string.Al_Insan),"076"));
        list.add(new SurahItem(context.getString(R.string.Al_Mursalat),"077"));
        list.add(new SurahItem(context.getString(R.string.An_Naba),"078"));
        list.add(new SurahItem(context.getString(R.string.An_Naziat),"079"));
        list.add(new SurahItem(context.getString(R.string.Abasa),"080"));
        list.add(new SurahItem(context.getString(R.string.At_Takwir),"081"));
        list.add(new SurahItem(context.getString(R.string.Al_Infitar),"082"));
        list.add(new SurahItem(context.getString(R.string.Al_Mutaffifin),"083"));
        list.add(new SurahItem(context.getString(R.string.Al_Inshiqaq),"084"));
        list.add(new SurahItem(context.getString(R.string.Al_Buruj),"085"));
        list.add(new SurahItem(context.getString(R.string.At_Tariq),"086"));
        list.add(new SurahItem(context.getString(R.string.Al_Ala),"087"));
        list.add(new SurahItem(context.getString(R.string.Al_Ghashiyah),"088"));
        list.add(new SurahItem(context.getString(R.string.Al_Fajr),"089"));
        list.add(new SurahItem(context.getString(R.string.Al_Balad),"090"));
        list.add(new SurahItem(context.getString(R.string.Ash_Shams),"091"));
        list.add(new SurahItem(context.getString(R.string.Al_Lail),"092"));
        list.add(new SurahItem(context.getString(R.string.Ad_Duha),"093"));
        list.add(new SurahItem(context.getString(R.string.Ash_Sharh),"094"));
        list.add(new SurahItem(context.getString(R.string.At_Tin),"095"));
        list.add(new SurahItem(context.getString(R.string.Al_Alaq),"096"));
        list.add(new SurahItem(context.getString(R.string.Al_Qadr),"097"));
        list.add(new SurahItem(context.getString(R.string.Al_Baiyinah),"098"));
        list.add(new SurahItem(context.getString(R.string.Az_Zalzalah),"099"));
        list.add(new SurahItem(context.getString(R.string.Al_Adiyat),"100"));
//Line
        list.add(new SurahItem(context.getString(R.string.Al_Qariah),"101"));
        list.add(new SurahItem(context.getString(R.string.At_Takathur),"102"));
        list.add(new SurahItem(context.getString(R.string.Al_Asr),"103"));
        list.add(new SurahItem(context.getString(R.string.Al_Humazah),"104"));
        list.add(new SurahItem(context.getString(R.string.Al_Fil),"105"));
        list.add(new SurahItem(context.getString(R.string.Quraish),"106"));
        list.add(new SurahItem(context.getString(R.string.Al_Maun),"107"));
        list.add(new SurahItem(context.getString(R.string.Al_Kauthar),"108"));
        list.add(new SurahItem(context.getString(R.string.Al_Kafirun),"109"));
        list.add(new SurahItem(context.getString(R.string.An_Nasr),"110"));
        list.add(new SurahItem(context.getString(R.string.Al_Masad),"111"));
        list.add(new SurahItem(context.getString(R.string.Al_Ikhlas),"112"));
        list.add(new SurahItem(context.getString(R.string.Al_Falaq),"113"));
        list.add(new SurahItem(context.getString(R.string.An_Nas),"114"));

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
