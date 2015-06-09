package br.edu.fatecriopreto.centralestagios.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import br.edu.fatecriopreto.centralestagios.Activities.CurriculoActivity;
import br.edu.fatecriopreto.centralestagios.Entidades.Conhecimento;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class ListConhecimentosAdapter extends BaseAdapter {

        private Context context;
        private List<Conhecimento> listaConhecimentos;

        private boolean[] estaSelecionado = new boolean[variaveisGlobais.listConhecimentoCurso.size()];
        private boolean primeiraVez = true;

        public ListConhecimentosAdapter(Context context, List<Conhecimento> listaConhecimentos){
            this.context =  context;
            this.listaConhecimentos = listaConhecimentos;
        }

        public void adicionaConhecimento(Conhecimento conhecimento){
            this.listaConhecimentos.add(conhecimento);
        }

    Conhecimento conhecimento;

        @Override
        public int getCount() {
            return this.listaConhecimentos.size();
        }


        @Override
        public Conhecimento getItem(int posicao) {
            return this.listaConhecimentos.get(posicao);
        }

        @Override
        public long getItemId(int posicao) {
            return posicao;
        }


        @Override
        public View getView(final int posicao, View convertView, ViewGroup parent) {

            Log.d("tamanho lista: ", String.valueOf(estaSelecionado.length));

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

            conhecimento = new Conhecimento();
            conhecimento = listaConhecimentos.get(posicao);

            /**
             * Seta os valores nos TextView
             */
            textConhecimento.setText("" + conhecimento.getDescricao());
            checMarcado.setTag(conhecimento);

            boolean jaExiste = false;

            if(primeiraVez) {

                for (Conhecimento c : variaveisGlobais.listConhecimentoPerfil) {
                    if (!jaExiste) {
                        if (c.getDescricao().equals(conhecimento.getDescricao()) && c.getId() == conhecimento.getId()
                                && c.getStatus() == conhecimento.getStatus()) {

                            checMarcado.setChecked(true);
                            estaSelecionado[posicao] = true;
                            jaExiste = true;
                            if (!variaveisGlobais.listConhecimentoMarcados.contains(conhecimento.getId())) {
                                variaveisGlobais.listConhecimentoMarcados.add(conhecimento.getId());
                            }
                        } else {
                            checMarcado.setChecked(false);
                            estaSelecionado[posicao] = false;
                        }
                    }
                }
            }
            else {

                checMarcado.setChecked(estaSelecionado[posicao]);
            }


            checMarcado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox chk = (CheckBox) v;
                    Conhecimento aux = new Conhecimento();
                    aux = (Conhecimento)chk.getTag();
                    if(chk.isChecked()) {
                        variaveisGlobais.listConhecimentoMarcados.add(aux.getId());
                        //chk.setChecked(true);
                        primeiraVez = false;
                        estaSelecionado[posicao] = true;
                    } else {
                        variaveisGlobais.listConhecimentoMarcados.remove((Object)aux.getId());
                        Log.d("position:", String.valueOf(aux.getId() + " " + aux.getDescricao()));
                        //chk.setChecked(false);
                        primeiraVez = false;
                        estaSelecionado[posicao] = false;
                    }
                }
            });

            return view;
        }


}
