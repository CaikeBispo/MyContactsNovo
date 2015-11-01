package br.com.drummond.mycontacts.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import java.io.IOException;

import br.com.drummond.mycontacts.Formulario;
import br.com.drummond.mycontacts.MainActivity;
import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.mapa.MapaActivity;

/**
 * Created by fabianoabreu on 21/10/2015.
 */
public class NotificationUtils {
    public static PendingIntent criarPendingIntent(Context ctx,String title,String texto, int id,Contato contato){
        Intent resultIntent= new Intent(ctx,MapaActivity.class);

        resultIntent.putExtra("contatoMostrar", contato);
        resultIntent.putExtra("trace",true);
        //Enviando dados juntos
        /*Bundle b=new Bundle();
        b.putString("trace","ok");
        resultIntent.putExtras(b);*/

        //resultIntent.putExtras(b);

        //Simular uma pilha de atividades para que a app volte pra home da app e não pra home do SO
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(MapaActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(
                0,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void criarNotificacaoSimples(final Context ctx,String title,String texto, int id,Contato contato){
        PendingIntent resultPendingIntent = criarPendingIntent(ctx,title,texto,id,contato);


        NotificationCompat.Builder mBuilder=
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher_mini)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(resultPendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .setLights(Color.BLUE,1000,5000);

        SharedPreferences prefs=ctx.getSharedPreferences("mycontacts", ctx.MODE_PRIVATE);
        boolean vibrate=prefs.getBoolean("vibrate", false);
        if(vibrate) {
            mBuilder.setVibrate(new long[]{1, 1000, 500, 500});
        }
        mBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(texto);
        mBuilder.setStyle(bigText);

        Bitmap icon= BitmapFactory.decodeResource(ctx.getResources(),R.drawable.ic_launcher);
        mBuilder.setLargeIcon(icon);

        //Disparando a notificação
        NotificationManagerCompat nm = NotificationManagerCompat.from(ctx);
        nm.notify(id, mBuilder.build());
    }
}
