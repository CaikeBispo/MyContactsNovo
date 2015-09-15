package br.com.drummond.mycontacts.externals;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.drummond.mycontacts.R;
import br.com.drummond.mycontacts.R.drawable;
import br.com.drummond.mycontacts.lista.modelo.Contato;

public class ListaContatosAdapter extends BaseAdapter {

    private List<Contato> contatos;
    private FragmentActivity fragmentActivity;

    public ListaContatosAdapter(){

    }
    public ListaContatosAdapter(List<Contato> contatos, FragmentActivity fragmentActivity) {
        this.contatos = contatos;
        this.fragmentActivity = fragmentActivity;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        Contato contato = contatos.get(position);
        return contato.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Popula os itens da lista de contatos
        Contato contato=contatos.get(position);

        LayoutInflater inflater=fragmentActivity.getLayoutInflater();
        View linha=inflater.inflate(R.layout.linha_listagem, null);

        TextView name = (TextView) linha.findViewById(R.id.name);
        name.setText(contato.getNome());

        ImageView foto = (ImageView) linha.findViewById(R.id.foto);
        if(contato.getFoto() != null){

            Bitmap fotocontato=BitmapFactory.decodeFile(contato.getFoto());
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