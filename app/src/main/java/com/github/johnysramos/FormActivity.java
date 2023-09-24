package com.github.johnysramos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FormActivity extends AppCompatActivity {

    // Chaves intent campos
    public static final String NOME = "NOME";
    public static final String STATUS = "STATUS";
    public static final String EMPRESTADO = "EMPRESTADO";
    public static final String CONSOLE_PLATAFORMA = "CONSOLE_PLATAFORMA";

    private EditText editTextNome;
    private RadioGroup radioGroupStatus;
    private RadioButton radioButtonStatusNovo;
    private RadioButton radioButtonStatusUsado;
    private CheckBox checkBoxEmprestado;
    private Spinner spinnerConsolePlataforma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.editTextNome = findViewById(R.id.editTextNome);
        this.radioGroupStatus = findViewById(R.id.radioGroupStatus);
        this.radioButtonStatusNovo = findViewById(R.id.radioButtonNovo);
        this.radioButtonStatusUsado = findViewById(R.id.radioButtonUsado);
        this.checkBoxEmprestado = findViewById(R.id.checkBoxEmprestado);
        this.spinnerConsolePlataforma = findViewById(R.id.spinnerConsolePlataforma);

        this.populaSpinnerConsolePlataforma();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){

            if (bundle.getInt(ListActivity.MODO, ListActivity.NOVO) == ListActivity.NOVO){
                setTitle(getString(R.string.novo_jogo_eletronico));
            }
            else {
                setTitle(getString(R.string.alterar_jogo_eletronico));

                String nome = bundle.getString(NOME);
                int status = bundle.getInt(STATUS);
                boolean emprestado = bundle.getBoolean(EMPRESTADO);
                int console_plataforma = bundle.getInt(CONSOLE_PLATAFORMA);

                // Nome
                editTextNome.setText(nome);

                // Status
                switch (status) {

                    case 0:
                        radioButtonStatusNovo.setChecked(true);
                        break;

                    case 1:
                        radioButtonStatusUsado.setChecked(true);
                        break;

                    default:
                        break;
                }

                // Emprestado
                checkBoxEmprestado.setChecked(emprestado);

                // Console Plataforma
                spinnerConsolePlataforma.setSelection(console_plataforma);


            }
        }
    }

    private void populaSpinnerConsolePlataforma() {
        String[] nomesConsolesPlataformas = getResources().getStringArray(R.array.consoles_plataformas);
        List<String> listaConsolesPlataformas = new ArrayList<>();

        for (int i = 0; i < nomesConsolesPlataformas.length; i++) {
            listaConsolesPlataformas.add(nomesConsolesPlataformas[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaConsolesPlataformas);
        this.spinnerConsolePlataforma.setAdapter(adapter);
    }

    public void limpaCampos() {
        this.editTextNome.setText(null);
        this.radioGroupStatus.clearCheck();
        this.checkBoxEmprestado.setChecked(false);

        Toast.makeText(this, R.string.campos_limpados, Toast.LENGTH_SHORT).show();
    }

    public void salvar() {

        String nome = editTextNome.getText().toString();
        Status status;
        boolean emprestado = checkBoxEmprestado.isChecked();
        String consolePlataforma = (String) spinnerConsolePlataforma.getSelectedItem();

        if(nome.isEmpty()) {
            Toast.makeText(this, R.string.nome_nao_preenchido, Toast.LENGTH_LONG).show();
            this.editTextNome.requestFocus();
            return;
        }

        if(radioGroupStatus.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, R.string.status_nao_preenchido, Toast.LENGTH_LONG).show();
            this.radioGroupStatus.requestFocus();
            return;
        }

        if(consolePlataforma.isEmpty()) {
            Toast.makeText(this, R.string.console_plataforma_nao_preenchido, Toast.LENGTH_LONG).show();
            this.spinnerConsolePlataforma.requestFocus();
            return;
        }

        switch (radioGroupStatus.getCheckedRadioButtonId()) {
            case R.id.radioButtonNovo:
                status = Status.NOVO;
                break;

            case R.id.radioButtonUsado:
                status = Status.USADO;
                break;

            default:
                status = null;
                break;
        }

        Intent intent = new Intent();
        intent.putExtra(NOME, nome);
        intent.putExtra(STATUS, status.getNumeroStatus());
        intent.putExtra(EMPRESTADO, emprestado);
        intent.putExtra(CONSOLE_PLATAFORMA, consolePlataforma);

        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;

            case R.id.menuItemLimpar:
                limpaCampos();
                return true;

            case android.R.id.home:
                cancelar();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cancelar() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelar();
    }
}