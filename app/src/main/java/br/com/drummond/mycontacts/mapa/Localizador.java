package br.com.drummond.mycontacts.mapa;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Fabiano de Lima Abre on 29/09/2015.
 */
public class Localizador {

    private Context context; // context para Geocoder

    public Localizador(Context c){
        this.context=c;
    }
    public LatLng gettCoordenada(String endereco){
        //Especialista em pegar uma coordenada apartir de um endereco
        Geocoder geocoder=new Geocoder(context);

        try {
            List<Address> enderecos = geocoder.getFromLocationName(endereco,1); //endereco e quantidade de resultados
            if(!enderecos.isEmpty()){
               Address enderecolocalizado = enderecos.get(0);
                double latitude= enderecolocalizado.getLatitude();
                double longitude = enderecolocalizado.getLongitude();

                return new LatLng(latitude,longitude);
            }
            else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
