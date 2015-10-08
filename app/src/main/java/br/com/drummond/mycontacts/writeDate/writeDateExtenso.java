package br.com.drummond.mycontacts.writeDate;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Fabiano de Lima Abre on 10/10/2015.
 */
public class writeDateExtenso {
    public static String NomeDoMes(int i){
        String mes[] = {"janeiro", "fevereiro", "marco", "abril", "maio", "junho", "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};
        return(mes[i-1]);
    }

    public static String DiaDaSemana(int i) {
        String diasem[] = {"Domingo", "Segunda-feira", "Terca-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sabado"};
        return(diasem[i-1]);
    }

    public static String DataPorExtenso(java.util.Date dt) {
            // retorna os valores ano, mes e dia da variavel "dt"
        int d = dt.getDate();
        int m = dt.getMonth()+1;
        int a = dt.getYear()+1900;
         // retorna o dia da semana: 1=domingo, 2=segunda-feira, ..., 7=sabado
        Calendar data = new GregorianCalendar(a, m-1, d);
        int ds = data.get(Calendar.DAY_OF_WEEK);
        //return(d + " de " + NomeDoMes(m) + " de " + a + " (" + DiaDaSemana(ds) + ").");
        return (DiaDaSemana(ds)+", "+d+" de "+NomeDoMes(m)+" de "+a+" as "+dt.getHours()+":"+dt.getMinutes()+":"+dt.getSeconds());
    }

}
