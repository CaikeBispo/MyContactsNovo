package br.com.drummond.mycontacts.mapa;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.fragments.MapaFragment;
import br.com.drummond.mycontacts.lista.modelo.Contato;

public class MapaActivity extends FragmentActivity{
    private Contato contatoMostrar;
    private AtualizadorDePosicao atualizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        MapaFragment mapa=new MapaFragment();

        Intent intent = getIntent();
        contatoMostrar = (Contato) intent.getSerializableExtra("contatoMostrar");


        if(contatoMostrar != null){
            //Caso tenha contato pra mostrar
            mapa.setContatoMostrar(contatoMostrar);
            ft.replace(R.id.mapa, mapa);
            ft.commit();
        }
        else{
            ft.replace(R.id.mapa, mapa);
            ft.commit();
            atualizador = new AtualizadorDePosicao(this,mapa); //Pegando nossa localização atual
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(contatoMostrar == null) {
            atualizador.cancelar();
        }
    }
}
