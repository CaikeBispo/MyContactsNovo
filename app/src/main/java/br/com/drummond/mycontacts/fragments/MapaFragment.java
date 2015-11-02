package br.com.drummond.mycontacts.fragments;

import android.graphics.Color;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.mapa.Localizador;

import static com.google.android.gms.internal.zzid.runOnUiThread;

/**
 * Created by Fabiano de Lima Abre on 29/09/2015.
 */
public class MapaFragment extends SupportMapFragment{
    private Contato contatoMostrar;
    private Boolean trace;
    private LatLng RoteLatLong;
    private Polyline polyline;
    private List<LatLng> list;
    private long distance;
    @Override
    public void onResume() {
        super.onResume();


        //LatLng local = new LatLng(-23.588275,-46.632303); Passando a latitude e longitude direto
        //LatLng local = new Localizador(getActivity()).gettCoordenada("Rua Vergueiro,3185 Vila Mariana"); // Passando o endereco para um especialista em transformar em LatLong
        //centralizaNoLocal(local); O AtualizarDePosicao pegou esta tarefa de atualizar posicao no mapa

        //Comecando a pegar contatos proximos ao endereco

        if(Boolean.valueOf(String.valueOf(contatoMostrar == null))){
            //Exibe toda a lista de contatos no mapa
            ContatoDAO dao=new ContatoDAO(getActivity());
            List<Contato> contatos=dao.getLista();
            for (Contato contato: contatos){
                //Criar um marker no mapa pra cada contato
                if(!contato.getEndereco().isEmpty()) {
                    GoogleMap map = getMap();

                    LatLng localContato = new LatLng(contato.getLatitude(), contato.getLongitude());
                    //LatLng localContato = new Localizador(getActivity()).gettCoordenada(contato.getEndereco()); // transforma string em latlong

                    MarkerOptions options = new MarkerOptions().title(contato.getNome() + " / " + contato.getTelefone()).position(localContato); //Opcoes de como o marker sera criado
                    try {
                        map.addMarker(options);
                    }catch (Exception e){
                        if(contato.getEndereco().isEmpty()){
                            Toast.makeText(getActivity(),"O contato "+contato.getNome()+" nao possue endereco cadastrado!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"Para identificar a localizacao e necessario conexao com a internet! Verifique sua conexao.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            dao.close();
        }
        else{
            //Caso exista um contato na memoria, exibe apenas aquele contato no mapa
            Contato contato=getContatoMostrar();

            //Centraliza o mapa no endereco do contato
            try {
                GoogleMap map = getMap();
                LatLng local= new LatLng(contato.getLatitude(),contato.getLongitude());
                //LatLng local = new Localizador(getActivity()).gettCoordenada(contato.getEndereco()); // Passando o endereco para um especialista em transformar em LatLong

                if(getTrace()){
                    centralizaNoLocal(getRoteLatLong());
                    getRoute(getRoteLatLong(),local);
                    //Polyline line=map.addPolyline(new PolylineOptions().add(getRoteLatLong(),local).width(5).color(Color.BLUE));
                }
                else{
                    centralizaNoLocal(local);
                }

                MarkerOptions options= new MarkerOptions().title(contato.getNome()+" / "+contato.getTelefone()).position(local); //Opcoes de como o marker sera criado
                map.addMarker(options);
            }catch (Exception e){
                if(contato.getEndereco().isEmpty()){
                    Toast.makeText(getActivity(),"O contato "+contato.getNome()+" nao possue endereco cadastrado!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),"Para identificar a localizacao e necessario conexao com a internet! Verifique sua conexao.", Toast.LENGTH_LONG).show();
                }
            }
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

    public Boolean getTrace() {
        return trace;
    }

    public void setTrace(Boolean trace) {
        this.trace = trace;
    }

    public LatLng getRoteLatLong() {
        return RoteLatLong;
    }

    public void setRoteLatLong(LatLng roteLatLong) {
        RoteLatLong = roteLatLong;
    }

    /* ***************************************** ROTA ***************************************** */

    /*public void getRouteByGMAV2(View view) throws UnsupportedEncodingException {
        EditText etD = (EditText) findViewById(R.id.destination);
        String origin = URLEncoder.encode(etO.getText().toString(), "UTF-8");
        String destination = URLEncoder.encode(etD.getText().toString(), "UTF-8");

        getRoute(new LatLng(-20.195403, -40.234478), new LatLng(-20.304596, -40.291813));
    }*/





    // WEB CONNECTION
    //public void getRoute(final String origin, final String destination){
    public void getRoute(final LatLng origin, final LatLng destination){
        new Thread(){
            public void run(){
						/*String url= "http://maps.googleapis.com/maps/api/directions/json?origin="
								+ origin+"&destination="
								+ destination+"&sensor=false";*/
                String url= "http://maps.googleapis.com/maps/api/directions/json?origin="
                        + origin.latitude+","+origin.longitude+"&destination="
                        + destination.latitude+","+destination.longitude+"&sensor=false";


                HttpResponse response;
                HttpGet request;
                AndroidHttpClient client = AndroidHttpClient.newInstance("route");

                request = new HttpGet(url);
                try {
                    response = client.execute(request);
                    final String answer = EntityUtils.toString(response.getEntity());

                    runOnUiThread(new Runnable(){
                        public void run(){
                            try {
                                //Log.i("Script", answer);
                                list = buildJSONRoute(answer);
                                drawRoute();
                            }
                            catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }




    // PARSER JSON
    public List<LatLng> buildJSONRoute(String json) throws JSONException {
        JSONObject result = new JSONObject(json);
        JSONArray routes = result.getJSONArray("routes");

        distance = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");

        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        List<LatLng> lines = new ArrayList<LatLng>();

        for(int i=0; i < steps.length(); i++) {
            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng"));


            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for(LatLng p : decodePolyline(polyline)) {
                lines.add(p);
            }

            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng"));
        }

        return(lines);
    }




    // DECODE POLYLINE
    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> listPoints = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            Log.i("Script", "POL: LAT: "+p.latitude+" | LNG: "+p.longitude);
            listPoints.add(p);
        }
        return listPoints;
    }

    public void drawRoute(){
        PolylineOptions po;

        if(polyline == null){
            po = new PolylineOptions();

            for(int i = 0, tam = list.size(); i < tam; i++){
                po.add(list.get(i));
            }

            GoogleMap map = getMap();
            po.color(Color.BLUE).width(4);
            polyline = map.addPolyline(po);
        }
        else{
            polyline.setPoints(list);
        }
    }
}
