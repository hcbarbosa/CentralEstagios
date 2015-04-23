package br.edu.fatecriopreto.centralestagios;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;


public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.MyViewHolder>{

    private final LayoutInflater inflater;
    List<Information> listMenu = Collections.emptyList();
    public InformationAdapter(Context context, List<Information> lista){
       inflater = LayoutInflater.from(context);
       this.listMenu = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        //MyViewHolder holder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information atual = listMenu.get(position);
        holder.title.setText(atual.title);
        holder.icon.setImageResource(atual.iconid);
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
}
