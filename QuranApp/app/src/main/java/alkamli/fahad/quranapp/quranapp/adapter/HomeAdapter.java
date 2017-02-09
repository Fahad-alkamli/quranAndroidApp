package alkamli.fahad.quranapp.quranapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import alkamli.fahad.quranapp.quranapp.CommonFunctions;
import alkamli.fahad.quranapp.quranapp.PlayerActivity;
import alkamli.fahad.quranapp.quranapp.PlayerActivity2;
import alkamli.fahad.quranapp.quranapp.R;
import alkamli.fahad.quranapp.quranapp.entity.SurahItem;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<SurahItem> SourahList=new ArrayList<SurahItem>();
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,line;
        public View view;
        public MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            line = (TextView) view.findViewById(R.id.line);
             this.view=view;
        }
    }


    public HomeAdapter(Context context,ArrayList<SurahItem> SourahList) {
        this.context = context;
        this.SourahList=SourahList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View CustomView = inflater.inflate(R.layout.single_list_item, parent, false);
        this.context=parent.getContext();
        return new MyViewHolder(CustomView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String sourahTitle=SourahList.get(position).getTitle();
        final String order=SourahList.get(position).getOrder();
        holder.title.setText(sourahTitle);
        holder.view.setTag(sourahTitle);
        holder.view.setClickable(true);
        //Make sure the first separation line is invisible
        if(position==0)
        {
            holder.line.setVisibility(View.GONE);
        }else{
            holder.line.setVisibility(View.VISIBLE);
        }
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=((String) view.getTag());
                //Toast.makeText(context,((String) view.getTag()),Toast.LENGTH_LONG).show();
                Intent i;
                if(CommonFunctions.getSharedPreferences(context).getBoolean("textButtons",false))
                {
                    i=new Intent(context, PlayerActivity2.class);
                }else{
                    i=new Intent(context, PlayerActivity.class);
                }
                i.putExtra("title",title);
                i.putExtra("order",order);
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return SourahList.size();
    }
}
