package br.com.drummond.mycontacts.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.drummond.mycontacts.MainActivity;
import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.adapter.ContatoAdapter;
import br.com.drummond.mycontacts.adapter.LigacaoAdapter;
import br.com.drummond.mycontacts.interfaces.RecyclerViewOnClickListenerHack;
import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.dao.LigacaoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.lista.modelo.Ligacao;

/**
 * Created by Fabiano de Lima on 09/07/2015.
 */
public class Historico extends Fragment implements RecyclerViewOnClickListenerHack, View.OnClickListener{
private RecyclerView mRecyclerView;
    private List<Contato> mList;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list_historico);
        mRecyclerView.setHasFixedSize(true);

        //chamada do mGestured
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        //Criando um linear Layout no modo default para listagem de contatos e setando-o no nosso RecyclerView
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        //Carregando a lista, e chamando o adapter para trabalhar com os eventos e insero de dados nos componentes
        carregaLista();

        return view;
    }

    public void onResume() {
        super.onResume();

        LigacaoDAO dao = new LigacaoDAO(getActivity());
        List<Ligacao> listAux = dao.getListaLigacao();
        dao.close();

        //Log.i("Lista","Teste: "+listAux.size());
        LigacaoAdapter adapter = new LigacaoAdapter(getActivity(), listAux);
        //adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter( adapter );
    }

    public void carregaLista(){
        LigacaoDAO dao = new LigacaoDAO(getActivity());
        List<Ligacao> listAux = dao.getListaLigacao();
        dao.close();

        LigacaoAdapter adapter = new LigacaoAdapter(getActivity(), listAux);
        //adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClickListener(View view, int position) {
        //Pegando a lista de liga��es
        LigacaoDAO dao = new LigacaoDAO(getActivity());
        List<Ligacao> listAux = dao.getListaLigacao();
        dao.close();

        //Pegando a liga��o clicada e populando um contato para ser salvo novamente nas liga��es
        Ligacao ligacao= listAux.get(position);
        Contato contato=new Contato();
        contato.setId(ligacao.getIdContato());
        contato.setNome(ligacao.getNome());
        contato.setTelefone(ligacao.getTelefone());
        contato.setFoto(ligacao.getFoto());

        //Chaando o metodo para call do intent e salvando a liga��o
        LigacaoAdapter adapter = (LigacaoAdapter) mRecyclerView.getAdapter();
        getActivity().startActivity(adapter.dial(contato.getTelefone()));
        dao.salva(contato); //Salvando conte�do
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        //Pegando a lista de liga��es
        LigacaoDAO dao = new LigacaoDAO(getActivity()); //Chamada o SQLITE
        List<Ligacao> listAux = dao.getListaLigacao();
        dao.close();

        //Pegando a liga��o clicada e populando um contato para ser salvo novamente nas liga��es
        Ligacao ligacao= listAux.get(position);

        Toast.makeText(getActivity(),"Long: "+ligacao.getTelefone(),Toast.LENGTH_SHORT).show();
        dao.deletar(ligacao);
        dao.close();
        //Chaando o metodo para call do intent e salvando a liga��o
        LigacaoAdapter adapter = (LigacaoAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.historico, menu); //Aqui deve ser R.menu.principal, mas nao consigo mecher
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.delregistro) {
            LigacaoDAO daoLig = new LigacaoDAO(getActivity());
            daoLig.removeAll();
            daoLig.close();
            carregaLista();
            return true;
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
