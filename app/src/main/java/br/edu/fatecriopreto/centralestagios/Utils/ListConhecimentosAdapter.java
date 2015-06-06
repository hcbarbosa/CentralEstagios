package br.edu.fatecriopreto.centralestagios.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;

import java.util.List;

import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.R;

public class ListConhecimentosAdapter extends BaseAdapter {

        private Context context;
        private List<Conhecimento> listaConhecimentos;


        public ListConhecimentosAdapter(Context context, List<Conhecimento> listaConhecimentos){
            this.context =  context;
            this.listaConhecimentos = listaConhecimentos;
        }

        public void adicionaConhecimento(Conhecimento conhecimento){
            this.listaConhecimentos.add(conhecimento);
        }


        @Override
        public int getCount() {
            return this.listaConhecimentos.size();
        }


        @Override
        public Object getItem(int posicao) {
            return this.listaConhecimentos.get(posicao);
        }

        @Override
        public long getItemId(int posicao) {
            return posicao;
        }


        @Override
        public View getView(int posicao, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(R.layout.lista_item_conhecimento, null);
            }

            /**
             * Recupero os compoentes que estao dentro do lista
             */
            TextView textConhecimento = (TextView) view.findViewById(R.id.txtConhecimento);
            CheckBox checMarcado = (CheckBox) view.findViewById(R.id.chec_seleciona_conhecimento);


            Conhecimento conhecimento = listaConhecimentos.get(posicao);

            /**
             * Seta os valores nos TextView
             */
            textConhecimento.setText("" + conhecimento.getDescricao());
            checMarcado.setChecked(conhecimento.isEstaSelecionado());

            return view;
        }


}