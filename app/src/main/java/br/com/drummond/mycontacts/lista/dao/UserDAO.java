package br.com.drummond.mycontacts.lista.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import br.com.drummond.mycontacts.lista.modelo.User;

/**
 * Created by Bispo Caike on 10/10/2015.
 */
public class UserDAO extends SQLiteOpenHelper{
    private static final String DATABASE = "Lista de Contatos 3";
    private static final int VERSAO = 1;
    private static final Context Register = null;

    public UserDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    //Table name
    private static final String TableUser = "user";

    //Name of atributes
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_RESET = "reset";
    private static final String KEY_IS_LOGADO = "isLogado";
    private static final String KEY_FOTO = "foto";
    private static final String KEY_RECUPERAR_SENHA = "recuperarSenha";

    private static final String [] COLUMNS = {KEY_ID, KEY_NAME,
            KEY_LAST_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_RESET, KEY_FOTO, KEY_RECUPERAR_SENHA};

    public void addUser(User user){
        //Log.d("addUser", user.getName().toString() + user.getLastName().toString() + user.getEmail().toString() + user.getPassword().toString() + user.getFoto().toString());
        Log.d("foto -------> ", user.getFoto());

        //Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //Create content Values to add key "Column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_RESET, user.getReset());
        //values.put(KEY_IS_LOGADO, user.getisLogado());
        values.put(KEY_FOTO, user.getFoto());
        Log.d("foto ---> ---> ", user.getFoto());
        values.put(KEY_RECUPERAR_SENHA, user.getRecoverPass());
        Log.d("RPass---> ---> ", user.getRecoverPass());
        //Inserting datas
        db.insert(TableUser, //table
                null, //NullColumnHack
                values);

        //Closing DB
        db.close();
        Log.d("teste", "sqliteFoi");
    }
    /*public User getUser(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //build the query
        Cursor cursor =
                db.query(TableUser, // a. table
                        COLUMNS, // b. column names
                        " id = 2", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(KEY_NAME, KEY_LAST_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_RESET, KEY_IS_LOGADO, KEY_FOTO);
        user.setName(cursor.getString(1));
        user.setLastName(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        user.setFoto(cursor.getString(4));

        //Registering log
        Log.d("getUser("+id+")", user.toString());

        return user;
    }
    */
    public List<User> getUser(String inputName){
        List<User> users = new LinkedList<User>();
        String query = "SELECT * FROM " + TableUser + " WHERE name = '"+ inputName +"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        User user = null;
        if(cursor.moveToFirst()){

            do {
                user = new User(KEY_NAME, KEY_LAST_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_RESET, KEY_IS_LOGADO, KEY_FOTO, KEY_RECUPERAR_SENHA);
                //user.setName(cursor.getString(1));
                user.setName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setPassword(cursor.getString(4));
                user.setEmail(cursor.getString(3));
                user.setFoto(cursor.getString(6));
                user.setRecoverPass(cursor.getString(7));
                users.add(user);
            } while (cursor.moveToNext());
        }
        for(User teste: users)
            Log.i("Recuperarsenha -> ", teste.getRecoverPass());

        return users;
    }

    public List<User> getAnswer(String paramSenha){
        List<User> users = new LinkedList<User>();
        String query = "SELECT * FROM " + TableUser + " WHERE recuperarSenha = '"+ paramSenha +"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        User user = null;
        if(cursor.moveToFirst()){
            do {
                user = new User(KEY_NAME, KEY_LAST_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_RESET, KEY_IS_LOGADO, KEY_FOTO, KEY_RECUPERAR_SENHA);
                //user.setName(cursor.getString(1));
                user.setName(cursor.getString(1));
                user.setLastName(cursor.getString(2));
                user.setPassword(cursor.getString(4));
                user.setEmail(cursor.getString(3));
                user.setFoto(cursor.getString(6));
                if(paramSenha.equals(paramSenha)){
                    user.setRecoverPass(cursor.getString(7));}
                users.add(user);
            } while (cursor.moveToNext());
        }
        for(User teste: users)
            Log.i("Recuperar senha -> ", teste.getRecoverPass());

        return users;
    }

    public List<User> getAllUsers(String inputName, String inputPassword){
        List<User> users = new LinkedList<User>();
        String query = "SELECT * FROM " + TableUser + " WHERE name = '"+ inputName +"' and password = '"
                + inputPassword + "'"  ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        User user = null;
        if(cursor.moveToFirst()){
            do {
                user = new User(KEY_NAME, KEY_LAST_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_RESET, KEY_IS_LOGADO, KEY_FOTO, KEY_RECUPERAR_SENHA);
                //user.setName(cursor.getString(1));
                user.setName(cursor.getString(1));
                //user.setLastName(cursor.getString(2));

                users.add(user);
            } while (cursor.moveToNext());
        }
        for(User teste: users)
            Log.i("getAllUsers() name -> ", teste.getName());
        return users;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String DDL = "CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE, lastName TEXT, email TEXT, " +
                "password TEXT, reset TEXT, foto TEXT, recuperarSenha TEXT" +
                ");";
        db.execSQL(DDL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DDL = "DROP TABLE IF EXISTS user";
        db.execSQL(DDL);
        this.onCreate(db);
    }
}

