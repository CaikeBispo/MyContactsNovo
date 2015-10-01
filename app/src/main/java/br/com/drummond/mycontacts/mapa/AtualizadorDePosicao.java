package br.com.drummond.mycontacts.mapa;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import br.com.drummond.mycontacts.fragments.MapaFragment;

/**
 * Created by Fabiano de Lima Abre on 29/09/2015.
 */
public class AtualizadorDePosicao implements LocationListener {
    //Especialista em atualizar a nossa posicao
    private LocationManager locationManager;
    private MapaFragment mapa;

    public AtualizadorDePosicao(Activity activity,MapaFragment mapa){
        //trabalhando com o GPS
        this.mapa=mapa;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        //Pedindo update da nossa location
        String provider="gps";
        long tempoMin=20000; //20 sg
        float distanciaMin=20; //20m
        locationManager.requestLocationUpdates(provider,tempoMin,distanciaMin,this);
    }

    @Override
    public void onLocationChanged(Location novaLocalizacao) {
        //Quando posicao mudou podemos centralizar o mapa no local do gps
        double latitude=novaLocalizacao.getLatitude();
        double longitude=novaLocalizacao.getLongitude();

        LatLng local = new LatLng(latitude,longitude);
        mapa.centralizaNoLocal(local);

    }

    public void cancelar() {
        //Destruir o listener do atualizador
        locationManager.removeUpdates(this); // Passa o listener que  a propria classe como parametro
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Mudou o tipo de provedor de location

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
