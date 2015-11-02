package br.com.drummond.mycontacts;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import br.com.drummond.mycontacts.lista.dao.UserDAO;
import br.com.drummond.mycontacts.lista.modelo.User;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class Autenticacao extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        final UserDAO db = new UserDAO(this);

        Button recoverPassPressed;
        recoverPassPressed = (Button) findViewById(R.id.recoverPass);
        recoverPassPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Recover pressed", LENGTH_SHORT).show();

            }
        });

        Button loginPressed;
        //loginPressed = (Button)findViewById(R.id.entrar);
        loginPressed = (Button) findViewById(R.id.entrar);
        final EditText user = (EditText) findViewById(R.id.user);
        final EditText pass = (EditText) findViewById(R.id.pass);

        loginPressed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String inputName = user.getText().toString();
                String inputPassword = pass.getText().toString();
                List<User> list = db.getAllUsers(inputName, inputPassword);
                if (list.size() >= 1) {
                    Intent startCadastro = new Intent(Autenticacao.this, MainActivity.class);
                    startCadastro.putExtra("nameCad", inputName);
                    startActivity(startCadastro);
                }
                else
                    makeText(getApplicationContext(), "Usuario ou senha invalidos", Toast.LENGTH_LONG).show();
            }
        });

        Button  registerPressed;
        registerPressed = (Button)findViewById(R.id.register);
        registerPressed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startCadastro = new Intent(Autenticacao.this, Cadastro.class);

                startActivity(startCadastro);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_autenticacao, menu);
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