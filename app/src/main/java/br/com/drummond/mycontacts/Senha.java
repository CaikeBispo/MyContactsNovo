package br.com.drummond.mycontacts;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import br.com.drummond.mycontacts.lista.dao.UserDAO;
import br.com.drummond.mycontacts.lista.modelo.User;


public class Senha extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha);
        final EditText tip = (EditText)findViewById(R.id.perguntaSecreta);
        final TextView returnPass = (TextView)findViewById(R.id.retornaSenha);
        String a = tip.getText().toString();
        final String passFromDB;

        if(a.length() >= 2) {

            final UserDAO db = new UserDAO(this);
            List<User> list = db.getAnswer(tip.getText().toString());
            final User user = list.get(0);
            passFromDB = user.getRecoverPass();
            Log.d("Pass filho da mae -> ", user.getRecoverPass().toString());
        }

        else
            passFromDB = " ";


        Button actionRec;
        actionRec = (Button)findViewById(R.id.recoverPassword);
        actionRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //returnPass.setText("Sua senha e: " + tip.getText());
                if(passFromDB.length() > 3)
                    returnPass.setText("Sua senha e: " + passFromDB);
                else {
                    final UserDAO dbase = new UserDAO(Senha.this);
                    List<User> lista = dbase.getAnswer(tip.getText().toString());
                    //List<User> lista = dbase.getUser("manuelad");
                    final User usuario = lista.get(0);
                    returnPass.setText("Seu usario e: '"+usuario.getName()+"'"+" e sua senha e: '" +
                        usuario.getPassword()+"'");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_senha, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
