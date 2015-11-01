package br.com.drummond.mycontacts.mapa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import br.com.drummond.mycontacts.MainActivity;
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
            final Handler handler = new Handler();
            new Thread(){
                @Override
                public void run() {
                    try {
                        handler.post(new Runnable() {
                            public void run() {
                                for(int i=0;i<mList.size();i++){
                                    if(!mList.get(i).getEndereco().isEmpty()){
                                        double distance=0;
                                        LatLng localContato = new LatLng(mList.get(i).getLatitude(),mList.get(i).getLongitude());
                                        distance = distance(local,localContato);
                                        if(distance < 3000){
                                            int id=6565;
                                            SharedPreferences prefs=activity.getSharedPreferences("mycontacts",activity.MODE_PRIVATE);
                                            boolean notefy=prefs.getBoolean("notifications", false);
                                            if(notefy){
                                                NotificationUtils.criarNotificacaoSimples(activity, "Você está próximo do "+mList.get(i).getNome(),"Você está a apenas "+String.format("%.3f",distance)+" mts. Que tal passar para tomar uma xícara de café?!", id);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    } catch (final Exception e) {
                        e.getMessage();
                    }
                }
            }.start();
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
        AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(activity);
        caixaDialogo.setTitle("Ative seu gps!");
        caixaDialogo.setMessage("Para maior proveito do que o MyContacts pode lhe proporcionar, ative o gps do seu aparelho.");

        caixaDialogo.create();
        caixaDialogo.show();
    }
}
