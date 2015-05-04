package br.edu.fatecriopreto.centralestagios.Menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.R;


public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    List<Information> listMenu = Collections.emptyList();
    private Context context;
    private ClickListener clickListener;

    public InformationAdapter(Context context, List<Information> lista) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        listMenu = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        return (new MyViewHolder(view));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Information atual = listMenu.get(position);
        holder.title.setText(atual.title);
        holder.icon.setImageResource(atual.iconid);

    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return listMenu.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);

        }


        @Override
        public void onClick(View v) {
            //abre uma activity dependendo do menu q ser clicar
            //context.startActivity(new Intent(context, PerfilActivity.class));

            if(clickListener != null){
                clickListener.itemClicked(v, getPosition());
            }
        }
    }


        public interface ClickListener{
                void itemClicked(View view, int position);
        }

}
