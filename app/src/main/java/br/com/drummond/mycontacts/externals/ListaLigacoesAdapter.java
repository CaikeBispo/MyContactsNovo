package br.com.drummond.mycontacts.externals;

import java.util.List;

import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.lista.modelo.Ligacao;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaLigacoesAdapter extends BaseAdapter{

    private List<Ligacao> ligacoes;
    private FragmentActivity fragmentActivity;

    public ListaLigacoesAdapter(List<Ligacao> ligacoes, FragmentActivity fragmentActivity){
        this.ligacoes = ligacoes;
        this.fragmentActivity = fragmentActivity;

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ligacoes.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return ligacoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        Ligacao ligacao = ligacoes.get(position);
        return ligacao.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Ligacao ligacao=ligacoes.get(position);
        Contato contato=new Contato();

        LayoutInflater inflater=fragmentActivity.getLayoutInflater();
        View linha=inflater.inflate(R.layout.linha_listagem, null);

        TextView name = (TextView) linha.findViewById(R.id.name);
        name.setText(ligacao.getTelefone());

        TextView phone = (TextView) linha.findViewById(R.id.phone);
        phone.setText(ligacao.getTelefone());

        ImageView foto = (ImageView) linha.findViewById(R.id.foto);

        if(ligacao.getFoto() != null){
            Log.i("FOTO", "FOTOTESTE");
            Bitmap fotocontato=BitmapFactory.decodeFile(ligacao.getFoto());
            Bitmap fotoreduzida = Bitmap.createScaledBitmap(fotocontato, 150, 150, true);

            foto.setImageBitmap(fotoreduzida);
        }
        else{
            Drawable draw=fragmentActivity.getResources().getDrawable(R.drawable.ic_action_person);
            foto.setImageDrawable(draw);
        }
        return linha;
    }

}