package br.edu.fatecriopreto.centralestagios.Menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;


public class InformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final LayoutInflater inflater;
    List<Information> listMenu = Collections.emptyList();
    private Context context;
    private ClickListener clickListener;
    private int oi;

    public InformationAdapter(Context context, List<Information> lista) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        listMenu = lista;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View view = inflater.inflate(R.layout.drawer_header, parent, false);

            return (new HeaderHolder(view));
        }else {
            View view = inflater.inflate(R.layout.custom_row, parent, false);
            return (new ItemHolder(view));
        }
    }

    @Override
    public int getItemViewType(int position){
        if(position==0){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(!(holder instanceof  HeaderHolder)){
            ItemHolder itemHolder = (ItemHolder) holder;
            Information atual = listMenu.get(position-1);
            itemHolder.title.setText(atual.title);
            itemHolder.icon.setImageResource(atual.iconid);
        }

    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return listMenu.size()+1;

    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public ItemHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);

        }

        @Override
        public void onClick(View v) {

            if(clickListener != null){
                clickListener.itemClicked(v, getPosition());
            }
        }
    }


    class HeaderHolder extends RecyclerView.ViewHolder{

        public HeaderHolder(View itemView) {
            super(itemView);

            ImageView img = (ImageView) itemView.findViewById(R.id.image_profile);
            TextView name = (TextView) itemView.findViewById(R.id.nameUser);
            TextView email = (TextView) itemView.findViewById(R.id.emailUser);

            //alterando dados do usuario no menu
            name.setText(variaveisGlobais.getUserName());
            img.setImageDrawable(itemView.getResources().getDrawable(variaveisGlobais.getImageUser()));
            email.setText(variaveisGlobais.getUserEmail());
        }
    }


        public interface ClickListener{
                void itemClicked(View view, int position);
        }

}
