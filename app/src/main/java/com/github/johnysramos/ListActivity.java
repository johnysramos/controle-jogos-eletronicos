package com.github.johnysramos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ActionMode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.johnysramos.persistencia.ControleJogosEletronicosDatabase;
import com.github.johnysramos.utils.UtilsGUI;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // Chave intent operações CRUD
    public static final String MODO = "MODO";
    // Modos CRUD
    public static final int NOVO = 1;
    public static final int ALTERAR = 2;

    public static final String ARQUIVO_SHARED_PREFERENCES = "com.github.johnysramos.ControleJogosEletronicos.PREFERENCIAS_NIGHT_MODE";
    public static final String NIGHT_MODE_KEY = "NIGHT_MODE";

    public static int nightModeValue;

    private ListView listViewJogosEletronicos;
    JogoEletronicoAdapter jogoEletronicoAdapter;
    private List<JogoEletronico> jogosEletronicos;

    private ActionMode actionMode;
    private int        posicaoSelecionada = -1;
    private View       viewSelecionada;

    private ControleJogosEletronicosDatabase database;


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.menu_list_selected, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch(item.getItemId()){
                case R.id.menuItemEditar:
                    onClickEditar();
                    mode.finish();
                    return true;

                case R.id.menuItemExcluir:
                    onClickExcluir();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            if (viewSelecionada != null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode         = null;
            viewSelecionada    = null;

            listViewJogosEletronicos.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        database = ControleJogosEletronicosDatabase.getDatabase(ListActivity.this);

        listViewJogosEletronicos = findViewById(R.id.listViewJogosEletronicos);

        listViewJogosEletronicos.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view,
                                        int position,
                                        long id) {

                    JogoEletronico jogoEletronico = (JogoEletronico) listViewJogosEletronicos.getItemAtPosition(position);

                    Toast.makeText(getApplicationContext(),
                            getString(R.string.jogo_eletronico) + " " + jogoEletronico.getNome() + " " + getString(R.string.foi_clicado),
                            Toast.LENGTH_LONG).show();
                }
            });

        listViewJogosEletronicos.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   View view,
                                                   int position,
                                                   long id) {

                        if (actionMode != null){
                            return false;
                        }

                        posicaoSelecionada = position;

                        view.setBackgroundColor(nightModeValue == AppCompatDelegate.MODE_NIGHT_NO ?
                                                    Color.LTGRAY :
                                                    Color.DKGRAY);

                        viewSelecionada = view;

                        listViewJogosEletronicos.setEnabled(false);

                        actionMode = startSupportActionMode(mActionModeCallback);

                        return true;
                    }
                });

        buscaPreferenciasNightMode();
        popularLista();
    }

    private void buscaPreferenciasNightMode() {
        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        nightModeValue = sharedPreferences.getInt(NIGHT_MODE_KEY, AppCompatDelegate.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(nightModeValue);
    }

    private void salvaPreferenciasNightMode() {
        AppCompatDelegate.setDefaultNightMode(nightModeValue);

        SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NIGHT_MODE_KEY, nightModeValue);
        editor.apply();
    }

    public void onClickSobre() {
        Intent intent = new Intent(this, SobreActivity.class);
        startActivity(intent);
    }

    public void onClickNovo() {
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra(MODO, NOVO);
        startActivityForResult(intent, NOVO);
    }

    public void onClickEditar() {

        JogoEletronico jogoEletronico = jogosEletronicos.get(posicaoSelecionada);

        Intent intent = new Intent(this, FormActivity.class);

        intent.putExtra(MODO, ALTERAR);

        intent.putExtra(FormActivity.NOME, jogoEletronico.getNome());
        intent.putExtra(FormActivity.STATUS, jogoEletronico.getStatus());
        intent.putExtra(FormActivity.EMPRESTADO, jogoEletronico.isEmprestado());
        intent.putExtra(FormActivity.CONSOLE_PLATAFORMA, jogoEletronico.getConsolePlataforma());

        startActivityForResult(intent, ALTERAR);
    }

    public void onClickExcluir() {
        String mensagem = getString(R.string.deseja_realmente_excluir_esse_jogo)
                 + "\n" + jogosEletronicos.get(posicaoSelecionada).getNome();

        DialogInterface.OnClickListener listener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    switch(which){
                        case DialogInterface.BUTTON_POSITIVE:
                            database.jogoEletronicoDao().delete(jogosEletronicos.get(posicaoSelecionada));
                            popularLista();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

        UtilsGUI.confirmaAcao(this, mensagem, listener);
    }

    private void popularLista() {
        jogosEletronicos =  database.jogoEletronicoDao().queryAll();
        jogoEletronicoAdapter = new JogoEletronicoAdapter(this, jogosEletronicos);
        listViewJogosEletronicos.setAdapter(jogoEletronicoAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Bundle bundle = data.getExtras();

            String nome = bundle.getString(FormActivity.NOME);
            int status = bundle.getInt(FormActivity.STATUS);
            boolean emprestado = bundle.getBoolean(FormActivity.EMPRESTADO);
            String consolePlataforma = bundle.getString(FormActivity.CONSOLE_PLATAFORMA);

            if (requestCode == ALTERAR){
                database.jogoEletronicoDao().update(
                            trataJogoEletronico(
                                    nome,
                                    status,
                                    emprestado,
                                    consolePlataforma,
                                    jogosEletronicos.get(posicaoSelecionada)));

                popularLista();
                posicaoSelecionada = -1;

            }else{
                JogoEletronico jogoEletronico = trataJogoEletronico(nome, status, emprestado, consolePlataforma, null);
                database.jogoEletronicoDao().insert(jogoEletronico);
                popularLista();
            }

            jogoEletronicoAdapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.jogo_eletronico_salvo_com_sucesso, Toast.LENGTH_LONG).show();
        }
    }

    private JogoEletronico trataJogoEletronico(String nome, int status, boolean emprestado, String consolePlataforma, JogoEletronico jogoEletronico) {

        if (jogoEletronico == null) {
            jogoEletronico = new JogoEletronico();
        }

        Status[] statusList = Status.values();
        ConsolePlataforma[] consolePlataformasList = ConsolePlataforma.values();

        int consolePlataformaFinal = -1;

        for (int i = 0; i < consolePlataformasList.length; i++) {
            if (getString(consolePlataformasList[i].getstringResourceDescricao()).equals(consolePlataforma)) {
                consolePlataformaFinal = i;
            }
        }

        jogoEletronico.setNome(nome);
        jogoEletronico.setStatus(status);
        jogoEletronico.setEmprestado(emprestado);
        jogoEletronico.setConsolePlataforma(consolePlataformaFinal);

        return jogoEletronico;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemModoNoturno = menu.findItem(R.id.menuItemModoNoturno);
        menuItemModoNoturno.setChecked(nightModeValue == AppCompatDelegate.MODE_NIGHT_YES);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemAdicionar:
                onClickNovo();
                return true;

            case R.id.menuItemSobre:
                onClickSobre();
                return true;

            case R.id.menuItemModoNoturno:
                item.setChecked(!(nightModeValue == AppCompatDelegate.MODE_NIGHT_YES));
                nightModeValue = item.isChecked() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
                salvaPreferenciasNightMode();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}