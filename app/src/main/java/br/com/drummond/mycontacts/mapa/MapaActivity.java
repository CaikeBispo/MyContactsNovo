package br.com.drummond.mycontacts.mapa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.drummond.mycontacts.MainActivity;
import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.fragments.MapaFragment;
import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;

public class MapaActivity extends ActionBarActivity{
    private Contato contatoMostrar;
    private AtualizadorDePosicao atualizador;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        mToolbar = (Toolbar) findViewById(R.id.tb_map);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MapaFragment mapa=new MapaFragment();

        Intent intent = getIntent();
        contatoMostrar = (Contato) intent.getSerializableExtra("contatoMostrar");


        if(contatoMostrar != null){
            Bundle b=new Bundle();
            Toast.makeText(this,"Bundle "+intent.getSerializableExtra("trace"), Toast.LENGTH_SHORT).show();
            Boolean trace= (Boolean) intent.getSerializableExtra("trace");
            String nome;
            if(contatoMostrar.getNome().isEmpty()){
                nome=contatoMostrar.getTelefone();
            }
            else{
                nome=contatoMostrar.getNome();
            }
            //Caso tenha contato pra mostrar
            if(trace != null){
                if(trace){
                    mToolbar.setTitle("Rota ate: "+nome);
                    setSupportActionBar(mToolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    mapa.setTrace(true);
                }
                else{
                    mToolbar.setTitle("Localizacao de: "+nome);
                    setSupportActionBar(mToolbar);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    mapa.setTrace(false);
                }
            }
            else{
                mToolbar.setTitle("Localizacao de: "+nome);
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mapa.setTrace(false);
            }
            mapa.setContatoMostrar(contatoMostrar);
            ft.replace(R.id.mapa, mapa);
            ft.commit();
        }
        else{
            mToolbar.setTitle("Localizacao dos contatos");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ft.replace(R.id.mapa, mapa);
            ft.commit();
            //atualizador.cancelar();
            atualizador = new AtualizadorDePosicao(this,mapa); //Pegando nossa localizacao atual
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(contatoMostrar == null) {
            atualizador.cancelar();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem itemSelect) {
        int itemClicado = itemSelect.getItemId();//descobrir qual o item clicado
        switch (itemClicado) {
            case android.R.id.home:
                Intent irHome=new Intent(this,MainActivity.class);
                startActivity(irHome);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(itemSelect);
    }
}
