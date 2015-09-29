package br.com.drummond.mycontacts.fragments;

import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.mapa.Localizador;

/**
 * Created by Fabiano de Lima Abre on 29/09/2015.
 */
public class MapaFragment extends SupportMapFragment{
    @Override
    public void onResume() {
        super.onResume();


        //LatLng local = new LatLng(-23.588275,-46.632303); Passando a latitude e longitude direto
        //LatLng local = new Localizador(getActivity()).gettCoordenada("Rua Vergueiro,3185 Vila Mariana"); // Passando o endereço para um especialista em transformar em LatLong
        //centralizaNoLocal(local); O AtualizarDePosicao pegou esta tarefa de atualizar posicao no mapa

        //Começando a pegar contatos proximos ao endereço
        ContatoDAO dao=new ContatoDAO(getActivity());
        List<Contato> contatos=dao.getLista();
        for (Contato contato: contatos){
            //Criar um marker no mapa pra cada contato
            GoogleMap map = getMap();
            LatLng localContato = new Localizador(getActivity()).gettCoordenada(contato.getEndereco()); // transforma string em latlong
            MarkerOptions options= new MarkerOptions().title(contato.getNome()+" / "+contato.getTelefone()).position(localContato); //Opcoes de como o marker sera criado
            map.addMarker(options);
        }
        dao.close();
    }

    public void centralizaNoLocal(LatLng local) {
        //Pega LatLong e centraliza no mapa
        GoogleMap map =getMap();
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(local, 15); //Fabrica com alguns updates da api
        map.animateCamera(update);
    }
}
