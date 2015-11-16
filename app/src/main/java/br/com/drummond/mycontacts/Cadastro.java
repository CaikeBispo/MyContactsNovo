package br.com.drummond.mycontacts;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.drummond.mycontacts.lista.dao.UserDAO;
import br.com.drummond.mycontacts.lista.modelo.User;

import static android.widget.Toast.makeText;


public class Cadastro extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_OPEN = 1;
    String mCurrentPhotoPath;
    static File caminhoFoto = null;


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
        editMail = (EditText) findViewById(R.id.email);
        editPass = (EditText) findViewById(R.id.pass);
        editPassConfirm = (EditText) findViewById(R.id.passCofirm);



        //final String pass;
        final String pass = editPass.getText().toString();
        final String passConfirm = editPassConfirm.getText().toString();

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (caminhoFoto == null){
                    Toast.makeText(getApplication(), "Insira sua imagem de perfil", Toast.LENGTH_SHORT).show();
                }

                else if (editName.getText().length() == 0){
                    editName.requestFocus();
                    //Log.i("Erro", "editname");
                    Toast.makeText(getApplication(), "Preencha seu nome", Toast.LENGTH_SHORT).show();
                }
                else if(!isValidEmail(editMail.getText().toString())) {
                    //Log.i("Erro", "editmail");
                    Toast.makeText(getApplication(), "E-mail invalido", Toast.LENGTH_SHORT).show();
                }
                else if (editPass.getText().length() < 3){
                    //Log.i("Erro", "senha ");
                    Toast.makeText(getApplication(), "Senha invalida, digite ao menos 3 digitos ", Toast.LENGTH_SHORT).show();
                }
                else if (editPassConfirm.getText().length() < 3){
                    //Log.i("Erro", "Confirmar senha ");
                    Toast.makeText(getApplication(), "Confirmar senha invalida, digite ao menos 3 digitos  ", Toast.LENGTH_SHORT).show();
                }
                else if (editPass.getText().toString().equals(editPassConfirm.getText().toString())) {
                    Log.d("teste edit name",(editMail.getText().toString()));
                    db.addUser(new User(editName.getText().toString(), editSurname.getText().toString(), editMail.getText().toString(), editPass.getText().toString(), reset, isLogado, caminhoFoto.toString()));
                    db.close();

                    makeText(getApplicationContext(), "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT).show();

                    // getting back to mainActivity
                    String subName = editName.getText().toString();
                    String subMail = editMail.getText().toString();
                    Intent startMainActivity = new Intent(Cadastro.this, MainActivity.class);
                    startMainActivity.putExtra("nameCad", subName);
                    startActivity(startMainActivity);
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
        String nomeFoto = DateFormat.format(
                "yyyy-MM-dd_hhmmss", new Date()).toString();

                caminhoFoto = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), nomeFoto);
        Intent it = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(caminhoFoto));
        startActivityForResult(it, 0);
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
        super.onActivityResult(
                requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 0) {
            ImageView img = (ImageView)
                    findViewById(R.id.startCamera);

            // Obtem o tamanho da ImageView
            int targetW = img.getWidth();
            int targetH = img.getHeight();

            // Obtem a largura e altura da foto
            BitmapFactory.Options bmOptions =
                    new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(
                    caminhoFoto.getAbsolutePath(), bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determina o fator de redimensionamento
            int scaleFactor = Math.min(
                    photoW/targetW, photoH/targetH);

            // Decodifica o arquivo de imagem em
            // um Bitmap que preenchera a ImageView
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(
                    caminhoFoto.getAbsolutePath(), bmOptions);
            img.setImageBitmap(bitmap);
            //img.setImageDrawable(getDrawable(1), bitmap);
        }

        if (resultCode == RESULT_OK && requestCode == 2) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {
                    MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(
                    filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap yourSelectedImage =
                    BitmapFactory.decodeFile(filePath);

            ImageView imgView = (ImageView)findViewById(R.id.startCamera);
            imgView.setImageBitmap(yourSelectedImage);

            /*try {
                salvarFotoClick(imgView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            galleryButtonClick(imgView);*/
        }
    }

    public void salvarFotoClick(View v) throws FileNotFoundException {
        if (caminhoFoto != null && caminhoFoto.exists()) {
            MediaStore.Images.Media.insertImage(
                    getContentResolver(),
                    caminhoFoto.getAbsolutePath(),
                    caminhoFoto.getName(), "");

            Toast.makeText(this,
                    "Imagem adicionada a galeria.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void galleryButtonClick(View v) {

        Intent intent = new Intent(
                Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
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

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
