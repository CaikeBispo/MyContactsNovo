package br.com.drummond.mycontacts;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import br.com.drummond.mycontacts.lista.modelo.Contato;

public class FormularioHelper {

    private static final Context Formulario = null;
    private EditText editNome, editTelefone, editEmail, editEndereco;
    private ImageView foto;
    private ImageButton editOperadora;
    //private Spinner emailtipo;
    private Spinner spnOp, enderecotipo;
    private Contato contato;
    ArrayAdapter<String> adapterSpinner;
    ArrayAdapter<String> adapterSpinnerTipo;
    ArrayList<String> nomesOperadoras;

    public FormularioHelper(Formulario formulario) {
        editNome = (EditText) formulario.findViewById(R.id.nome);
        editTelefone = (EditText) formulario.findViewById(R.id.telefone);
        editEmail = (EditText) formulario.findViewById(R.id.email);
        editEndereco = (EditText) formulario.findViewById(R.id.endereco);
        foto = (ImageView) formulario.findViewById(R.id.foto);
        spnOp = (Spinner) formulario.findViewById(R.id.operadora);
        editOperadora = (ImageButton) formulario.findViewById(R.id.btnOp);
        //emailtipo= (Spinner) formulario.findViewById(R.id.emailtipo);
        enderecotipo= (Spinner) formulario.findViewById(R.id.enderecotipo);

        String[] nomesOperadoras ={"", "Claro BR", "Fixo", "Nextel", "Oi", "Tim", "Vivo", "Outro"};
        String[] tipos ={"Casa", "Trabalho", "Outro"};

        int layoutSpinner = android.R.layout.simple_spinner_item;

        adapterSpinner = new ArrayAdapter<String>(formulario, layoutSpinner, nomesOperadoras);
        adapterSpinnerTipo = new ArrayAdapter<String>(formulario, layoutSpinner, tipos);

        spnOp.setAdapter(adapterSpinner);
        //emailtipo.setAdapter(adapterSpinnerTipo);
        enderecotipo.setAdapter(adapterSpinnerTipo);

        contato = new Contato();
    }

    public Contato pegaContatoDoFormulario() {
        contato.setNome(editNome.getText().toString());
        contato.setTelefone(editTelefone.getText().toString());
        contato.setEmail(editEmail.getText().toString());
        //contato.setTipoemail((int) emailtipo.getSelectedItemId());
        contato.setEndereco(editEndereco.getText().toString());
        contato.setTipoendereco((int) enderecotipo.getSelectedItemId());
        contato.setOperadora((int) spnOp.getSelectedItemId());
        //contato.setStrOp(editOperadora.getText().toString());
        contato.setStrOp(spnOp.getSelectedItem().toString());

        return contato;
    }

    public void colocaContatoNoFormulario(Contato contatoMostrar, String mostrarOuAlterar) {
        contato = contatoMostrar;
        editNome.setText(contatoMostrar.getNome());
        editTelefone.setText(contatoMostrar.getTelefone());
        editEmail.setText(contatoMostrar.getEmail());
        //emailtipo.setSelection(contatoMostrar.getTipoemail());
        editEndereco.setText(contatoMostrar.getEndereco());
        enderecotipo.setSelection(contatoMostrar.getTipoendereco());

        int spnPosition = adapterSpinner.getPosition(contatoMostrar.getStrOp());
        spnOp.setSelection(spnPosition);
        //editOperadora.setText(contatoMostrar.getStrOp());

        if (contato.getFoto() != null) {
            carregaImagem(contatoMostrar.getFoto());
        }

        if (mostrarOuAlterar == "contatoMostrar") {
            editNome.setEnabled(false);
            editTelefone.setEnabled(false);
            editEmail.setEnabled(false);
            //emailtipo.setEnabled(false);
            editEndereco.setEnabled(false);
            editEndereco.setTextColor(Color.rgb(190, 190, 190));
            enderecotipo.setEnabled(false);
            foto.setEnabled(false);
            spnOp.setEnabled(false);
            editOperadora.setVisibility(View.INVISIBLE);
        }
    }

    public ImageView getFoto() {
        return foto;
    }

    public void carregaImagem(String caminhoArquivo) {
        contato.setFoto(caminhoArquivo);
        //Fazer imagem que foi tirada
        Bitmap imagem = BitmapFactory.decodeFile(caminhoArquivo);
		/*Reduzir imagem*/
        Bitmap imagemReduzida = Bitmap.createScaledBitmap(imagem, 150, 200, true);
        //Carregar imagem que foi tirada
        foto.setImageBitmap(imagemReduzida);
    }
}