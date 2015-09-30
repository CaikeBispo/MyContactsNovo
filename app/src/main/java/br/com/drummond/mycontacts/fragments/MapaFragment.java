package br.com.drummond.mycontacts.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;

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
    private Contato contatoMostrar;
    @Override
    public void onResume() {
        super.onResume();


        //LatLng local = new LatLng(-23.588275,-46.632303); Passando a latitude e longitude direto
        //LatLng local = new Localizador(getActivity()).gettCoordenada("Rua Vergueiro,3185 Vila Mariana"); // Passando o enderešo para um especialista em transformar em LatLong
        //centralizaNoLocal(local); O AtualizarDePosicao pegou esta tarefa de atualizar posicao no mapa

        //Comešando a pegar contatos proximos ao enderešo

        Log.i("Ver", String.valueOf(contatoMostrar == null));
        if(Boolean.valueOf(String.valueOf(contatoMostrar == null))){
            //Exibe toda a lista de contatos no mapa
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
        else{
            //Caso exista um contato na memoria, exibe apenas aquele contato no mapa
            Contato contato=getContatoMostrar();

            //Centraliza o mapa no enderešo do contato
            LatLng local = new Localizador(getActivity()).gettCoordenada(contato.getEndereco()); // Passando o enderešo para um especialista em transformar em LatLong
            centralizaNoLocal(local);

            GoogleMap map = getMap();
            LatLng localContato = new Localizador(getActivity()).gettCoordenada(contato.getEndereco()); // transforma string em latlong
            MarkerOptions options= new MarkerOptions().title(contato.getNome()+" / "+contato.getTelefone()).position(localContato); //Opcoes de como o marker sera criado
            map.addMarker(options);
        }

    }

    public void centralizaNoLocal(LatLng local) {
        //Pega LatLong e centraliza no mapa
        GoogleMap map =getMap();
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(local, 15); //Fabrica com alguns updates da api
        map.animateCamera(update);
    }

    public Contato getContatoMostrar() {
        return contatoMostrar;
    }

    public void setContatoMostrar(Contato contatoMostrar) {
        this.contatoMostrar = contatoMostrar;
    }
}
