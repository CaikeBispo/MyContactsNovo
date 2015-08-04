package br.com.drummond.mycontacts.fragments;

/**
 * Created by Fabiano de Lima on 11/07/2015.
 */
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.List;

import br.com.drummond.mycontacts.MainActivity;
import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.adapter.ContatoAdapter;
import br.com.drummond.mycontacts.interfaces.RecyclerViewOnClickListenerHack;
import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;


//FRAGMENT DA CLASSE DE LISTAGEM DE CONTATOS
/*Novidade:
  RecyclerView para criação de componentes na listagem de contatos, trabalhando em conjunto com o adapter.

 */
public class ContatoFragment extends Fragment implements RecyclerViewOnClickListenerHack, View.OnClickListener {

    private RecyclerView mRecyclerView;
    private List<Contato> mList;
    //private FloatingActionButton fab;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listagem_agenda, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);

        /*mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
            /*}
        });*/

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
        /*FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
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
        });*/

        //Clique no ACTION BUTTON chama metodo abaixo
        //fab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent abrirTecladoLigar = new Intent(Intent.ACTION_DIAL);
                startActivity(abrirTecladoLigar);
                break;
            default:
                Toast.makeText(getActivity(),"HUMM",Toast.LENGTH_SHORT).show();
        }
    }

    public void onResume() {
        super.onResume();

        ContatoDAO dao = new ContatoDAO(getActivity());
        List<Contato> listAux = dao.getLista();
        dao.close();

        ContatoAdapter adapter = new ContatoAdapter(getActivity(), listAux);
        adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter( adapter );
    }

    @Override
    public void onClickListener(View view, int position) {
        //Ao clicar em um dos itens da lista de contatos
        ContatoAdapter adapter = (ContatoAdapter) mRecyclerView.getAdapter();
        adapter.dial(position);

        getActivity().startActivity(adapter.dial(position));

    }

    public List<Contato> carregaLista(){
        ContatoDAO dao = new ContatoDAO(getActivity());
        List<Contato> listAux = dao.getLista();
        dao.close();
        return (listAux);
    }
}

