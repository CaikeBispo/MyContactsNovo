package br.com.drummond.mycontacts.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import br.com.drummond.mycontacts.Formulario;
import br.com.drummond.mycontacts.MainActivity;
import br.com.drummond.mycontacts.R;

/**
 * Created by fabianoabreu on 21/10/2015.
 */
public class NotificationUtils {
    public static PendingIntent criarPendingIntent(Context ctx,String title,String texto, int id){
        Intent resultIntent= new Intent(ctx,Formulario.class);

        //Enviando dados juntos
        Bundle b=new Bundle();
        b.putString("data","teste");

        resultIntent.putExtras(b);

        //Simular uma pilha de atividades para que a app volte pra home da app e não pra home do SO
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(ctx);
        stackBuilder.addParentStack(Formulario.class);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(
                0,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void criarNotificacaoSimples(Context ctx,String title,String texto, int id){
        PendingIntent resultPendingIntent = criarPendingIntent(ctx,title,texto,id);


        NotificationCompat.Builder mBuilder=
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_nav_cloud_upload)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(texto)
                        .setContentIntent(resultPendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .setLights(Color.BLUE,1000,5000);

        mBuilder.setVibrate(new long[]{1, 1000, 500, 500});
        mBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        //Disparando a notificação
        NotificationManagerCompat nm = NotificationManagerCompat.from(ctx);
        nm.notify(id, mBuilder.build());
    }
}
