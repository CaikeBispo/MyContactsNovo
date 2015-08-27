package br.com.drummond.mycontacts;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;
import java.util.zip.Inflater;

import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;

import static android.widget.Toast.*;


public class SearchableActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    ContatoDAO contatoDAO = new ContatoDAO(this);
    Contato contato = new Contato();
    Contato contatoAux = new Contato();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        /*mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("caike");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

    }

    private void setSupportActionBar(Toolbar mToolbar) {
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.search);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView( item );
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.abc_search_hint));

        Intent intent = getIntent();
        ContatoDAO contatoDAO = new ContatoDAO((Context) item);
        Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction());
        String q = intent.getStringExtra(SearchManager.QUERY);
        makeText(this, q, LENGTH_SHORT).show();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            MenuInflater inflater = getMenuInflater();
            String query = intent.getStringExtra(SearchManager.QUERY);
            makeText(this, query, LENGTH_SHORT).show();
        }

         for(int i = 0; i < contatoDAO.getLista().size(); i++){
            contatoDAO.getLista();
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        if(id == R.id.novo){
            startActivity(new Intent(this, Formulario.class));
        }

        return super.onOptionsItemSelected(item);
    }

}

