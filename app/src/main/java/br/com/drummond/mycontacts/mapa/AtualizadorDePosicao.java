package br.com.drummond.mycontacts.mapa;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import br.com.drummond.mycontacts.fragments.MapaFragment;
import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.notification.NotificationUtils;

/**
 * Created by Fabiano de Lima Abre on 29/09/2015.
 */
public class AtualizadorDePosicao implements LocationListener {
    //Especialista em atualizar a nossa posicao
    private LocationManager locationManager;
    private MapaFragment mapa;
    private Activity activity;

    private List<Contato> mList;

    public AtualizadorDePosicao(Activity a,MapaFragment mapa){
        //trabalhando com o GPS
        this.activity=a;
        this.mapa=mapa;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        //Pedindo update da nossa location
        String provider="gps";
        long tempoMin=20000; //20 sg
        float distanciaMin=20; //20m
        locationManager.requestLocationUpdates(provider,tempoMin,distanciaMin,this);
    }

    public AtualizadorDePosicao(Activity a){
        this.activity=a;
        //trabalhando com o GPS
        this.mapa=null;
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        ContatoDAO dao = new ContatoDAO(activity);
        mList = dao.getLista();
        dao.close();

        //Pedindo update da nossa location
        String provider="gps";
        long tempoMin=20000; //20 sg
        float distanciaMin=20; //20m

        locationManager.requestLocationUpdates(provider, tempoMin, distanciaMin, this);
    }

    @Override
    public void onLocationChanged(Location novaLocalizacao) {
        //Quando posicao mudou podemos centralizar o mapa no local do gps
        final double latitude=novaLocalizacao.getLatitude();
        final double longitude=novaLocalizacao.getLongitude();

        final LatLng local = new LatLng(latitude,longitude);

        if(this.mapa != null) {
            mapa.centralizaNoLocal(local);
        }
        else{
            //Log.i("tag", "CHEGOU");

            new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    Log.i("tag", "CHEGOU");
                    for(int i=0;i<mList.size();i++){
                        if(!mList.get(i).getEndereco().isEmpty()){
                            double distance=0;
                            LatLng localContato = null;
                            localContato = new Localizador(activity).gettCoordenada(mList.get(i).getEndereco()); // transforma string em latlong
                            distance = distance(local,localContato);
                            if(distance < 3000){
                                //Toast.makeText(activity,""+distance+" mts "+mList.get(i).getNome(),Toast.LENGTH_SHORT).show();
                                int id=6565;
                                NotificationUtils.criarNotificacaoSimples(activity, "A "+distance+" mts do "+mList.get(i).getNome()+"!!!","Que tal tomar um café? =D", id);
                            }
                        }
                    }
                }
            },
            3000);
        }
    }

    public void cancelar() {
        //Destruir o listener do atualizador
        locationManager.removeUpdates(this); // Passa o listener que  a propria classe como parametro
    }

    public static double distance(LatLng StartP, LatLng EndP) {
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6366000 * c;
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
