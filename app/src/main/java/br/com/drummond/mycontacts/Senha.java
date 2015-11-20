package br.com.drummond.mycontacts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha);

        /* Header teste */
        mToolbar= (Toolbar) findViewById(R.id.tb_senha);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Sobre o MyContacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText tip = (EditText)findViewById(R.id.perguntaSecreta);
        final TextView returnPass = (TextView)findViewById(R.id.retornaSenha);

        Button actionRec;
        actionRec = (Button)findViewById(R.id.recoverPassword);
        actionRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        final UserDAO dbase = new UserDAO(Senha.this);
                        List<User> lista = dbase.getAnswer(tip.getText().toString());
                        final User usuario = lista.get(0);
                        returnPass.setText("Seu usuario e: '" + usuario.getName() + "'" + " e sua senha e: '" +
                                usuario.getPassword() + "'");
                    }
                    catch (Exception e){
                        returnPass.setText("Nome incorreto!");
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
        Intent startHome = new Intent(Senha.this, Autenticacao.class);
        startActivity(startHome);

        return super.onOptionsItemSelected(item);
    }
}
