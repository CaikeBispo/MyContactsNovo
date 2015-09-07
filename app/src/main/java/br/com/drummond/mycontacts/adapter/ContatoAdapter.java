package br.com.drummond.mycontacts.adapter;

/**
 * Created by Fabiano de Lima on 11/07/2015.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.drummond.mycontacts.MainActivity;
import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.domain.ContextMenuItem;
import br.com.drummond.mycontacts.interfaces.RecyclerViewOnClickListenerHack;
import br.com.drummond.mycontacts.lista.dao.LigacaoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;

/**
 * Created by viniciusthiengo on 4/5/15.
 */
public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.MyViewHolder> {
    private Context context;
    private List<Contato> mList;
    private LayoutInflater mLayoutInflater;
    private FragmentActivity fragmentActivity;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private float scale;
    private int width, height, roundPixels;


    public ContatoAdapter(Context c, List<Contato> l){
        mList = l;
        context=c;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragmentActivity = fragmentActivity;

        scale = context.getResources().getDisplayMetrics().density;
        width = context.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;

        roundPixels = (int)(2 * scale + 0.5f);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_car, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        Log.i("LOG", "onBindViewHolder()");
        //myViewHolder.ivCar.setImageResource(mList.get(position).getFoto());
        if(mList.get(position).getFoto() != null){
            Bitmap fotocontato= BitmapFactory.decodeFile(mList.get(position).getFoto());
            Bitmap fotoreduzida = Bitmap.createScaledBitmap(fotocontato, 150, 150, true);

            myViewHolder.foto.setImageBitmap(fotoreduzida);
        }
        else{
            //Ainda não estou conseguindo colocar foto padrão para contatos, caso ainda não tenha foto do proprio contato
            //Drawable draw=fragmentActivity.getResources().getDrawable(R.drawable.ic_person);
            //myViewHolder.foto.setImageDrawable(draw);
        }
        myViewHolder.tvModel.setText(mList.get(position).getNome() );
        myViewHolder.tvBrand.setText( mList.get(position).getTelefone() );
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(Contato c, int position){
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public Intent dial(int position){
        //Metodo para chamada do teclado para realizar ligações
        Contato contatoLigar = (Contato) mList.get(position);
        Contato registroChamada = (Contato) mList.get(position);

        Intent irParaTelaDeDiscagem = new Intent(Intent.ACTION_CALL);
        Uri discarPara = Uri.parse("tel: " + mList.get(position).getTelefone());
        LigacaoDAO daoLigacao = new LigacaoDAO(context);
        daoLigacao.salva(registroChamada); //Salvando conteúdo

        irParaTelaDeDiscagem.setData(discarPara);

        return irParaTelaDeDiscagem;
    }

    public void teste(int position){
        Contato contatoLigar = (Contato) mList.get(position);
        //Toast.makeText(context,mList.get(position).getNome()+"",Toast.LENGTH_LONG).show();
        ListPopupWindow listPopupWindow= new ListPopupWindow(context);
        //listPopupWindow.setAnchorView();
        Toast.makeText(context,mList.get(position).getNome()+"",Toast.LENGTH_LONG).show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView foto;
        public TextView tvModel;
        public TextView tvBrand;

        public MyViewHolder(View itemView) {
            super(itemView);

            foto = (ImageView) itemView.findViewById(R.id.iv_car);
            tvModel = (TextView) itemView.findViewById(R.id.tv_model);
            tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }*/
            //Contato contatoLigar = (Contato) mList.get(position);
            //Toast.makeText(context,mList.get(position).getNome()+"",Toast.LENGTH_LONG).show();

            List<ContextMenuItem> itens=new ArrayList<>();
            itens.add(new ContextMenuItem("Ok"));

            ContextMenuAdapter adapter= new ContextMenuAdapter(context,itens);

            ListPopupWindow listPopupWindow= new ListPopupWindow(context);
            listPopupWindow.setAdapter(adapter);
            listPopupWindow.setAnchorView(v);
            listPopupWindow.setWidth((int) (240 * scale + 0.5f));
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(context, "Clicou", Toast.LENGTH_LONG).show();
                    //Toast.makeText(context,getAdapterPosition()+"UGuig",Toast.LENGTH_LONG).show();
                }
            });
            listPopupWindow.setModal(true);
            Toast.makeText(context,"UGuig",Toast.LENGTH_LONG).show();
            listPopupWindow.show();
        }
    }
}

