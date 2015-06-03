package br.edu.fatecriopreto.centralestagios.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.edu.fatecriopreto.centralestagios.Activities.VagaPesquisaAvancadaActivity;
import br.edu.fatecriopreto.centralestagios.R;
import br.edu.fatecriopreto.centralestagios.variaveisGlobais;

public class ListVagasAdapter extends BaseAdapter {

	private final Activity activity;
	private final ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;

	public ListVagasAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data=d;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.list_row, null);

		TextView title = (TextView)vi.findViewById(R.id.titlevaga);
		TextView company = (TextView)vi.findViewById(R.id.company);
		TextView salary = (TextView)vi.findViewById(R.id.salary);
		TextView candidate = (TextView)vi.findViewById(R.id.candidate);

		HashMap<String, String> lista;
		lista = data.get(position);

		// Setting all values in listview
		title.setText(lista.get(variaveisGlobais.KEY_TITLE));
		company.setText(lista.get(variaveisGlobais.KEY_COMPANY));
		salary.setText(lista.get(variaveisGlobais.KEY_SALARY));
		candidate.setText(lista.get(variaveisGlobais.KEY_CANDIDATE));
		return vi;
	}
}