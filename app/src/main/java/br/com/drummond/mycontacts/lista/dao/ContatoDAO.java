package br.com.drummond.mycontacts.lista.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import br.com.drummond.mycontacts.lista.modelo.Contato;


public class ContatoDAO extends SQLiteOpenHelper{
	/*Classe SQLiteOpenHelper é uma classe que possui falicidades para le dar com o SQLite
	Para salvar somos obrigados a criar um metodo onCreate*/

    private static final String DATABASE = "Lista de Contatos";
    private static final int VERSAO = 1;
    private static final Context Formulario = null;

    /*Constructor de argumentos.
    Parametro Context: serve para podermos ter acesso a recurso do aparelho
               Name: nome do DATABASE
               Factory: customer factory = null
               Version: versão do banco de dados*/
    public ContatoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    public void salva(Contato contato) {
        //Para inserir no SQLite
        //Os valores a serem inseridos são do tipo ContentValues.
        ContentValues values = new ContentValues();

        values.put("nome", contato.getNome());
        values.put("telefone", contato.getTelefone());
        values.put("email", contato.getEmail());
        values.put("tipoEmail", contato.getTipoemail());
        values.put("endereco", contato.getEndereco());
        values.put("tipoEndereco", contato.getTipoendereco());
        values.put("foto", contato.getFoto());
        values.put("operadora", contato.getStrOp());

        values.put("latitude", contato.getLatitude());
        values.put("longitude", contato.getLongitude());

        values.put("data_mapa", "");

        getWritableDatabase().insert("Contatos", null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE Contatos (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT UNIQUE NOT NULL, telefone TEXT, email TEXT, tipoEmail TEXT," +
                "endereco TEXT, tipoEndereco TEXT, foto TEXT, operadora TEXT,latitude REAL,longitude REAL, data_mapa TEXT);";
        db.execSQL(ddl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*Caso você erre alguma coisa na criação da Tabela (nome de coluna errado, etc)
		  Basta apenas mudar o número da versão lá em cima que automaticamente é chamado o metodo upgrade */

        String ddl = "DROP TABLE EXISTS Contatos";
        db.execSQL(ddl);

        this.onCreate(db);
    }

    public List<Contato> getLista() {
        /*
        Comentado , para começar a ordenar contatos em ordem crescente de acordo com nome
        String[] colunas = {"id", "nome", "telefone", "email", "tipoEmail","endereco","tipoEndereco" ,"foto", "operadora","latitude","longitude","data_mapa"};

        Cursor cursor = getWritableDatabase().query("Contatos", colunas, null, null, null, null, null);*/

        String query= "Select * from Contatos order by nome ASC";
        Cursor cursor = getWritableDatabase().rawQuery(query,null);

        //Retorna um Cursor, atraves dele que vamos buscar os dados.

        //moveToNext vai para o primeiro registro, no caso aponta todos os dados de um contato

        //cursor.moveToNext Retorna enquanto tiver registro

        ArrayList<Contato> arrContato = new ArrayList<Contato>();

        while (cursor.moveToNext()) {

            Contato contato = new Contato();

            contato.setId(cursor.getLong(0)); //Forma simplificada
            String nome = cursor.getString(1); //Forma maior
            contato.setNome(nome);				//^^^^^^
            contato.setTelefone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setTipoemail(cursor.getInt(4));
            contato.setEndereco(cursor.getString(5));
            contato.setTipoendereco(cursor.getInt(6));
            contato.setFoto(cursor.getString(7));
            //contato.setOperadora(cursor.getInt(9));
            contato.setStrOp(cursor.getString(8));
            contato.setLatitude(cursor.getDouble(9));
            contato.setLongitude(cursor.getDouble(10));

            contato.setData_mapa(cursor.getString(11));

            arrContato.add(contato);
        }
        return arrContato;
    }

    public List<Contato> listaSearch() {
        String[] colunas = {"id", "nome", "telefone", "email", "tipoEmail","endereco","tipoEndereco" ,"foto", "operadora","latitude","longitude","data_mapa"};

        Cursor cursor = getWritableDatabase().query("Contatos", colunas, null, null, null, null, null);
        //Retorna um Cursor, atraves dele que vamos buscar os dados.

        //moveToNext vai para o primeiro registro, no caso aponta todos os dados de um contato

        //cursor.moveToNext Retorna enquanto tiver registro

        ArrayList<Contato> arrContato = new ArrayList<Contato>();

        while (cursor.moveToNext()) {

            Contato contato = new Contato();

            contato.setId(cursor.getLong(0)); //Forma simplificada
            String nome = cursor.getString(1); //Forma maior
            contato.setNome(nome);                //^^^^^^
            contato.setTelefone(cursor.getString(2));
            contato.setEmail(cursor.getString(3));
            contato.setTipoemail(cursor.getInt(4));
            contato.setEndereco(cursor.getString(5));
            contato.setTipoendereco(cursor.getInt(6));
            contato.setFoto(cursor.getString(7));
            contato.setStrOp(cursor.getString(8));
            contato.setLatitude(cursor.getDouble(9));
            contato.setLongitude(cursor.getDouble(10));

            contato.setData_mapa(cursor.getString(11));

            arrContato.add(contato);
        }
        return arrContato;
    }


    public void deletar(Contato contato) {

        String[] args = {contato.getId().toString()};
        getWritableDatabase().delete("Contatos", "id=?", args);
        Log.i("TAG", "Clicou no deletar "+contato.getId());
		
		/*Log.i("TAG", "Clicou no deletar "+contato.getNome());
		getWritableDatabase().execSQL("Delete from Contatos where nome = '"+contato.getNome()+"'");
		*/
    }

    public void alterar(Contato contato) {
        ContentValues values = new ContentValues();

        values.put("nome", contato.getNome());
        values.put("telefone", contato.getTelefone());
        values.put("email", contato.getEmail());
        values.put("tipoEmail", contato.getTipoemail());
        values.put("endereco", contato.getEndereco());
        values.put("tipoEndereco", contato.getTipoendereco());
        values.put("foto", contato.getFoto());
        values.put("operadora", contato.getStrOp());

        values.put("latitude", contato.getLatitude());
        values.put("longitude", contato.getLongitude());

        values.put("data_mapa", contato.getData_mapa());

        String[] args = {contato.getId().toString()};
        getWritableDatabase().update("Contatos", values, "id=?", args);
    }

    public void alterarDataMapa(Contato contato) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = sdf.format(new Date());
        ContentValues values = new ContentValues();

        values.put("data_mapa",date);

        String[] args = {contato.getId().toString()};
        getWritableDatabase().update("Contatos", values, "id=?", args);
    }
}