package br.edu.fatecriopreto.centralestagios;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.MyViewHolder>{

    private final LayoutInflater inflater;
    List<Information> listMenu = Collections.emptyList();
    private Context context;

    public InformationAdapter(Context context, List<Information> lista){
       inflater = LayoutInflater.from(context);
       listMenu = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Information atual = listMenu.get(position);
        holder.title.setText(atual.title);
        holder.icon.setImageResource(atual.iconid);

    }

    @Override
    public int getItemCount() {
        return listMenu.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);

            icon.setOnClickListener(this);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getPosition();
            switch (position) {
                case 0: Toast.makeText(v.getContext(), "Clicou em: Inicio",Toast.LENGTH_SHORT).show();
                    break;
                case 1:  Toast.makeText(v.getContext(), "Clicou em: Perfil",Toast.LENGTH_SHORT).show();
                    break;
                case 2:  Toast.makeText(v.getContext(), "Clicou em: Curriculo",Toast.LENGTH_SHORT).show();
                    break;
                case 3:  Toast.makeText(v.getContext(), "Clicou em: Vagas",Toast.LENGTH_SHORT).show();
                    break;
                case 4:  Toast.makeText(v.getContext(), "Clicou em: Mensagens",Toast.LENGTH_SHORT).show();
                    break;
                case 5:  Toast.makeText(v.getContext(), "Clicou em: Configuracoes",Toast.LENGTH_SHORT).show();
                    break;
                case 6:  Toast.makeText(v.getContext(), "Clicou em: Sair",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
