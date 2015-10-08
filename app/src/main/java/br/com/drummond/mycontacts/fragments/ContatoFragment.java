package br.com.drummond.mycontacts.fragments;

/**
 * Created by Fabiano de Lima on 11/07/2015.
 */
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.List;

import br.com.drummond.mycontacts.Formulario;
import br.com.drummond.mycontacts.MainActivity;
import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.adapter.ContatoAdapter;
import br.com.drummond.mycontacts.interfaces.RecyclerViewOnClickListenerHack;
import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.dao.LigacaoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.mapa.MapaActivity;


//FRAGMENT DA CLASSE DE LISTAGEM DE CONTATOS
/*Novidade:
  RecyclerView para criação de componentes na listagem de contatos, trabalhando em conjunto com o adapter.

 */
public class ContatoFragment extends Fragment implements RecyclerViewOnClickListenerHack, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<Contato> mList;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listagem_agenda, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                ContatoAdapter adapter = (ContatoAdapter) mRecyclerView.getAdapter();


                List<Contato> listAux = carregaLista();

                /*if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    List<Contato> listAux = carregaLista();

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }
                }*/
            }
        });

        //chamada do mGestured
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(),mRecyclerView,this));

        //Criando um linear Layout no modo default para listagem de contatos e setando-o no nosso RecyclerView
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        //Recebendo a lista de contatos
        ContatoDAO dao = new ContatoDAO(getActivity());
        List<Contato> mList = dao.getLista();
        dao.close();

        //Carregando a lista, e chamando o adapter para trabalhar com os eventos e inserção de dados nos componentes
        mList = carregaLista();
        ContatoAdapter adapter = new ContatoAdapter(getActivity(), mList);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter( adapter );

        //FLOATING ACTION BUTTON
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToRecyclerView(mRecyclerView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {

            }

            @Override
            public void onScrollUp() {

            }
        },new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                ContatoAdapter adapter = (ContatoAdapter) mRecyclerView.getAdapter();


                List<Contato> listAux = carregaLista();
            }
        });

        //Clique no ACTION BUTTON chama metodo abaixo
        fab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent abrirTecladoLigar = new Intent(Intent.ACTION_DIAL);
                abrirTecladoLigar.setData(Uri.parse("tel:"));
                startActivity(abrirTecladoLigar);
                break;
        }
    }

    public void onResume() {
        super.onResume();

        ContatoDAO dao = new ContatoDAO(getActivity());
        List<Contato> listAux = dao.getLista();
        dao.close();

        ContatoAdapter adapter = new ContatoAdapter(getActivity(), listAux);
        //adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickListener(View view, int position) {
        //Ao clicar em um dos itens da lista de contato

        List<Contato> listAux = carregaLista();
        final Contato contato=listAux.get(position);
        String op_outro = contato.getStrOp();

        //Log.i("Minha Operadora", MainActivity.operator);

        if (MainActivity.operator.equals(op_outro)) {
            efetuarLigacao(contato.getTelefone(), contato);

        } else {
            AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(getActivity());
            caixaDialogo.setTitle("Operadora diferente!");
            caixaDialogo.setMessage("Sua Operadora é "+MainActivity.operator +" e você está ligando para um "+op_outro+".\n\nDeseja continuar a ligação?");

            caixaDialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    efetuarLigacao(contato.getTelefone(), contato);
                }
            });

            caixaDialogo.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            caixaDialogo.create();
            caixaDialogo.show();
        }
    }

    private void efetuarLigacao(String contatoTelefone, Contato contato){
        Intent irParaTelaDeDiscagem = new Intent(Intent.ACTION_CALL);
        Uri discarPara = Uri.parse("tel: " + contatoTelefone);

        irParaTelaDeDiscagem.setData(discarPara);

        startActivity(irParaTelaDeDiscagem);

        LigacaoDAO daoLigacao = new LigacaoDAO(getActivity());
        daoLigacao.salva(contato); //Salvando conteúdo
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        ContatoAdapter adapter = (ContatoAdapter) mRecyclerView.getAdapter();
        adapter.createContextMenu(view, position);

    }
    public List<Contato> carregaLista() {
        ContatoDAO dao = new ContatoDAO(getActivity());
        List<Contato> listAux = dao.getLista();
        dao.close();
        return (listAux);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu); //Aqui deve ser R.menu.principal, mas nao consigo mecher

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.search);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            searchView = (SearchView) item.getActionView();
        }
        else{
            searchView = (SearchView) MenuItemCompat.getActionView(item);
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.abc_search_hint));

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.novo){
            startActivity(new Intent(getActivity(), Formulario.class));
        }
        if(id == R.id.map){
            Intent irParaMapa = new Intent(getActivity(), MapaActivity.class);
            startActivity(irParaMapa);
        }

        return super.onOptionsItemSelected(item);
    }

    //Iniciando o contato com longPress
    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;


        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh){
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY()); //Coordenadas da tela

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv,
                                rv.getChildPosition(cv) );
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildPosition(cv) );
                    }

                    return(true);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
    }



}

