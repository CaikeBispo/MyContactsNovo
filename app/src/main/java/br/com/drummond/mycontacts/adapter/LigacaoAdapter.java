package br.com.drummond.mycontacts.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.interfaces.RecyclerViewOnClickListenerHack;
import br.com.drummond.mycontacts.lista.dao.LigacaoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.lista.modelo.Ligacao;
import br.com.drummond.mycontacts.writeDate.writeDateExtenso;

/**
 * Created by Fabiano de Lima Abre on 09/08/2015.
 */
public class LigacaoAdapter extends RecyclerView.Adapter<LigacaoAdapter.MyViewHolder> {
    private List<Ligacao> mList;
    private LayoutInflater mLayoutInflater;
    private FragmentActivity fragmentActivity;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private Context context;

    public LigacaoAdapter(Context c, List<Ligacao> l){
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context=c;
        this.fragmentActivity = fragmentActivity;
    }

    public Intent dial(String telefone){
        //Metodo para chamada do teclado para realizar ligacoes

        Intent irParaTelaDeDiscagem = new Intent(Intent.ACTION_CALL);
        Uri discarPara = Uri.parse("tel: " + telefone);

        irParaTelaDeDiscagem.setData(discarPara);

        return irParaTelaDeDiscagem;
    }

    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
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
        //myViewHolder.ivCar.setImageResource(mList.get(position).getFoto());
        if(mList.get(position).getFoto() != null){
            Bitmap fotocontato= BitmapFactory.decodeFile(mList.get(position).getFoto());
            Bitmap fotoreduzida = Bitmap.createScaledBitmap(fotocontato, 120, 120, true);

            myViewHolder.foto.setImageBitmap(RoundImage.getRoundedCornerBitmap(fotoreduzida));
        }
        else{
            Drawable draw=context.getResources().getDrawable(R.drawable.ic_action_person_round);
            myViewHolder.foto.setImageDrawable(draw);
        }
        myViewHolder.tvModel.setText(mList.get(position).getNome());
        myViewHolder.tvBrand.setText(mList.get(position).getTelefone());
        if(mList.get(position).getOpTelein().isEmpty()){
            myViewHolder.operadora.setText("");
        }
        else {
            myViewHolder.operadora.setText(mList.get(position).getOpTelein()+" - ");
            switch (mList.get(position).getOpTelein()){
                case "Tim":
                    myViewHolder.operadora.setTextColor(context.getResources().getColor(R.color.tim));
                    break;
                case "Claro BR":
                    myViewHolder.operadora.setTextColor(context.getResources().getColor(R.color.clarobr));
                    break;
                case "Nextel":
                    myViewHolder.operadora.setTextColor(context.getResources().getColor(R.color.nextel));
                    break;
                case "Oi":
                    myViewHolder.operadora.setTextColor(context.getResources().getColor(R.color.oi));
                    break;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            java.util.Date date=format.parse(mList.get(position).getHoraligacao());
            myViewHolder.tvHour.setText(writeDateExtenso.DataPorExtenso(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView foto;
        public TextView tvModel;
        public TextView tvBrand;
        public TextView tvHour;
        public TextView operadora;

        public MyViewHolder(View itemView) {
            super(itemView);

            foto = (ImageView) itemView.findViewById(R.id.iv_car);
            tvModel = (TextView) itemView.findViewById(R.id.tv_model);
            tvBrand = (TextView) itemView.findViewById(R.id.tv_brand);
            tvHour= (TextView) itemView.findViewById(R.id.tv_hour);
            operadora= (TextView) itemView.findViewById(R.id.tv_operadora);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
