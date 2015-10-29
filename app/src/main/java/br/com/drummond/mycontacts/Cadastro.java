package br.com.drummond.mycontacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.drummond.mycontacts.lista.dao.UserDAO;
import br.com.drummond.mycontacts.lista.modelo.User;

import static android.widget.Toast.makeText;


public class Cadastro extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_OPEN = 1;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        final UserDAO db = new UserDAO(this);
        ImageView imageClick = (ImageView) findViewById(R.id.startCamera);
        imageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Click na imagem inicia captura de camera", Toast.LENGTH_SHORT).show();
                takeAPicture(view);
                //selectImage();

            }
        });



        Button registerUser;
        final EditText editName;
        //final String name = (String) findViewById(R.id.name).toString();
        final EditText editSurname;
        final EditText editMail;
        final EditText editPass;
        final EditText editPassConfirm;
        final String reset = "s";
        final String isLogado = "true";


        editName = (EditText) findViewById(R.id.name);
        editSurname = (EditText) findViewById(R.id.lastName);
        editMail = (EditText) findViewById(R.id.mail);
        editPass = (EditText) findViewById(R.id.pass);
        editPassConfirm = (EditText) findViewById(R.id.passCofirm);



        //final String pass;
        final String pass = editPass.getText().toString();
        final String passConfirm = editPass.getText().toString();


        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SQLiteUser db = new SQLiteUser(this);
                //makeText(getApplication(), pass + passConfirm, Toast.LENGTH_SHORT).show();

                //if(pass.equals(passConfirm)) {
                if(pass == pass) {
                    Log.d("teste edit name",(editName.getText().toString()));
                    db.addUser(new User(editName.getText().toString(), editSurname.getText().toString(), editMail.getText().toString(), editPass.getText().toString(), reset, isLogado));
                    String x = "ck";
                    String name = "ck";
                    db.close();
                    // get all books
                    /*List<User> list = db.getAllUsers(x);
                    if (list.size() == 1)
                    {


                    }*/
                    //db.getUser(1);

                    makeText(getApplicationContext(), "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT).show();

                    // getting back to mainActivity
                    Intent startCadastro = new Intent(Cadastro.this, MainActivity.class);
                    startActivity(startCadastro);

                }
                else
                    makeText(getApplicationContext(), "As senhas nao conferem", Toast.LENGTH_LONG).show();
            }
        });

                /*
                final EditText user = (EditText) findViewById(R.id.user);
                */

    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    public void takeAPicture(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 0);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();
            // Do work with full size photo saved at fullPhotoUri
            ...
        }
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                Bitmap img = (Bitmap) bundle.get("data");
                ImageView iv = (ImageView) findViewById(R.id.startCamera);
                iv.setImageBitmap(img);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
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
