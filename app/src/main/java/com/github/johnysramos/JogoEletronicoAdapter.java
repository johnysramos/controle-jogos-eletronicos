package com.github.johnysramos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class JogoEletronicoAdapter extends BaseAdapter {

    private Context context;
    private List<JogoEletronico> jogosEletronicos;

    private static class JogoEletronicoHolder {
        public TextView textViewValorNome;
        public TextView textViewValorStatus;
        public TextView textViewValorEmprestado;
        public TextView textViewValorConsolePlataforma;
    }

    public JogoEletronicoAdapter(Context context, List<JogoEletronico> jogosEletronicos) {
        this.context = context;
        this.jogosEletronicos = jogosEletronicos;
    }

    @Override
    public int getCount() {
        return jogosEletronicos.size();
    }

    @Override
    public Object getItem(int i) {
        return jogosEletronicos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        JogoEletronicoHolder holder;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_list_item, viewGroup, false);

            holder = new JogoEletronicoHolder();

            holder.textViewValorNome = view.findViewById(R.id.textViewValorNome);
            holder.textViewValorStatus = view.findViewById(R.id.textViewValorStatus);
            holder.textViewValorEmprestado = view.findViewById(R.id.textViewValorEmprestado);
            holder.textViewValorConsolePlataforma = view.findViewById(R.id.textViewValorConsolePlataforma);

            view.setTag(holder);

        } else {

            holder = (JogoEletronicoHolder) view.getTag();
        }

        // Preenche campo nome
        holder.textViewValorNome.setText(jogosEletronicos.get(i).getNome());

        // Preenche campo status
        switch (jogosEletronicos.get(i).getStatus()) {
            case 0:
                holder.textViewValorStatus.setText(R.string.novo);
                break;
            case 1:
                holder.textViewValorStatus.setText(R.string.usado);
                break;
        }

        // Peenche campo emprestado
        if (jogosEletronicos.get(i).isEmprestado()) {
            holder.textViewValorEmprestado.setText(R.string.sim);
        }
        else {
            holder.textViewValorEmprestado.setText(R.string.nao);
        }

        // Preenche campo console/plataforma
        switch (jogosEletronicos.get(i).getConsolePlataforma()) {
            case 0:
                holder.textViewValorConsolePlataforma.setText(R.string.playstation_5);
                break;
            case 1:
                holder.textViewValorConsolePlataforma.setText(R.string.xbox_one);
                break;
            case 2:
                holder.textViewValorConsolePlataforma.setText(R.string.computador);
                break;
            case 3:
                holder.textViewValorConsolePlataforma.setText(R.string.playstation_1);
                break;
            case 4:
                holder.textViewValorConsolePlataforma.setText(R.string.playstation_2);
                break;
        }

        return view;
    }
}

