package br.edu.fatecriopreto.centralestagios.Utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Entidades.Vaga;
import br.edu.fatecriopreto.centralestagios.R;

public class ListMensagemAdapter extends BaseAdapter {
        private List<Vaga> dataSource;
        private LayoutInflater inflater;

        public ListMensagemAdapter(List<Vaga> dataSource, Activity ctx) {
            this.dataSource = dataSource;
            this.inflater = LayoutInflater.from(ctx);
        }

        public List<Vaga> getDataSource() {
            return dataSource;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            // initIfNeed view
            //
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_chat, null);
                holder = new ViewHolder();
                holder.name = (TextView)convertView.findViewById(R.id.roomName);
                holder.naolidas = (TextView)convertView.findViewById(R.id.naolidas);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // populando
            Vaga dialog = dataSource.get(position);
            holder.name.setText(dialog.getDescricao());
            holder.naolidas.setText(R.string.qtdnlidas + "2");
            return convertView;
        }

        private static class ViewHolder{
            TextView name;
            TextView naolidas;
        }
    }
