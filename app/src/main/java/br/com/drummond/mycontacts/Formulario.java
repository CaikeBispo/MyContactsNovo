package br.com.drummond.mycontacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;

import java.io.File;

import br.com.drummond.mycontacts.lista.dao.ContatoDAO;

import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.mapa.Localizador;


public class Formulario extends ActionBarActivity {
    private Toolbar mToolbar;

    private FormularioHelper helper;
    private String caminhoArquivo;

    private Contato contatoMostrar;
    private Contato contatoAlterar;

    private Contato contato;
    private ContatoDAO dao;
    private ImageButton botao;
    private Button btnOp;
    private Spinner spnOp;
    private boolean i;
    private MainActivity MainActtivity;
    private EditText telDigitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        mToolbar= (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        contatoMostrar = (Contato) intent.getSerializableExtra("contatoMostrar");
        contatoAlterar = (Contato) intent.getSerializableExtra("contatoAlterar");

        //Extrair o codigo em uma classe separada, para evitar que a Activity cresça demais.
        helper = new FormularioHelper(this);

        telDigitado = (EditText) findViewById(R.id.telefone);

        botao = (ImageButton) findViewById(R.id.botao);

        botao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Contato contato = helper.pegaContatoDoFormulario();
                //especialista para salvar no Sqlite
                ContatoDAO dao = new ContatoDAO(Formulario.this);

                //Pega a coordenada para atualizar ou salvar o contato
                if(!contato.getEndereco().isEmpty()){
                    LatLng local = new Localizador(Formulario.this).gettCoordenada(contato.getEndereco());
                    contato.setLatitude(local.latitude);
                    contato.setLongitude(local.longitude);
                }

                if (contatoAlterar == null && contatoMostrar == null){
                    dao.salva(contato);
                } else if (contatoAlterar != null) {
                    //contato.setId(contato.getId());
                    dao.alterar(contato);
                }
                dao.close();

                finish(); //Botão voltar automatico
            }
        });

        btnOp = (Button) findViewById(R.id.btnOp);
        spnOp = (Spinner) findViewById(R.id.operadora);
        btnOp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Class_Operadora classe_op = new Class_Operadora();
                            final String NomeOperadora;
                            String response;

                            String telAjustado =  telDigitado.getText().toString();
                            telAjustado = telAjustado.replace("(", "");
                            telAjustado = telAjustado.replace(")", "");
                            telAjustado = telAjustado.replace("-", "");
                            telAjustado = telAjustado.replace(" ", "");

                            Log.i("Telefon Digitado: ", ""+telAjustado);


                            //String URL = "http://consultaoperadora1.telein.com.br/sistema/consulta_resumida.php?numero="+telAjustado+"&chave=74b451b7a6ef79a57085";
                            //SynchronousHttpConnection httpConnection = new SynchronousHttpConnection();
                            //response = httpConnection.post(URL);
                            response = "41#962301830";

                            NomeOperadora = classe_op.NomearOperadora(response);

                            Log.i("RESPONSE ", ": " + NomeOperadora);
                            handler.post(new Runnable() {
                                public void run() {
                                    // Aqui dentro do Handler atualiza a view com o retorno, dentro da Thread Main
                                    //TextView textViewSituacao  = (TextView) findViewById(R.id.textViewSituacao);
                                    //btnOp.setText(NomeOperadora);
                                    ArrayAdapter<String> adapterSpinner;
                                    int layoutSpinner = android.R.layout.simple_spinner_item;
                                    String[] arrayOperadoras ={"", "Claro BR", "Fixo", "Nextel", "Oi", "Tim", "Vivo", "Outro"};

                                    adapterSpinner = new ArrayAdapter<String>(getApplication(), layoutSpinner, arrayOperadoras);

                                    int spnPosition = adapterSpinner.getPosition(NomeOperadora);

                                    if (NomeOperadora != "Erro") {
                                        spnOp.setSelection(spnPosition);
                                    } else {
                                        AlertDialog.Builder caixaDialogo = new AlertDialog.Builder(Formulario.this);
                                        caixaDialogo.setTitle("Operadora não localizada.");
                                        caixaDialogo.setMessage("Verifique se você digitou o número corretamente. \nCaso esteja certo, por favor preencha a operadora através do combo");

                                        caixaDialogo.setNeutralButton("OK",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                          });

                                        spnOp.setSelection(0);

                                        caixaDialogo.create();
                                        caixaDialogo.show();
                                    }
                                }
                            });
                        } catch (final Exception e) {
                            e.getMessage();
                        }
                    }
                }.start();
            }
        });

        ImageView foto = helper.getFoto();
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                caminhoArquivo = Environment.getExternalStorageDirectory().toString()
                        +"/"+System.currentTimeMillis()+".png";

				/*Criando arquivo, deve ser um FILE*/
                File arquivo = new File(caminhoArquivo);

				/*Só que no Android a identificação de caminho tem que ser especificado em uma URI*/
                Uri localImagem = Uri.fromFile(arquivo);

				/*MediaStore.EXTRA_OUTPUT apelido conhecido pelo android para a Intent de camera saber o local da imagem*/
                irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localImagem);

				/*O codigo do resultado para tentativa de tirar foto é 123*/
                startActivityForResult(irParaCamera, 123);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(contatoMostrar != null){
            mToolbar.setTitle("Visualizar contato");
            botao.setVisibility(View.INVISIBLE);
            helper.colocaContatoNoFormulario(contatoMostrar, "contatoMostrar");
        } else if (contatoAlterar != null) {
            mToolbar.setTitle("Alterar contato");
            helper.colocaContatoNoFormulario(contatoAlterar, "contatoAlterar");
        }
        else{
            mToolbar.setTitle("Adicionar contato");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*Para saber se estamos voltando da chamada da camera comparamos o requestCode == 123*/
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {

                //ok para foto
                helper.carregaImagem(caminhoArquivo);
            } else {
                //cancelou foto
                caminhoArquivo = null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater(); //getMenuInflater retorna o menu inflate
        if(contatoMostrar != null){
            //Visualizar
            inflater.inflate(R.menu.viewer_contato, menu); //metodo inflate, devemos passar o xml que queremos inflar
        }
        else{
            inflater.inflate(R.menu.formularios, menu); //Editar formulario
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem itemSelect) {
        int itemClicado = itemSelect.getItemId();//descobrir qual o item clicado

        switch (itemClicado) {
            case android.R.id.home:
                Intent irHome=new Intent(this,MainActivity.class);
                startActivity(irHome);
                break;
            case R.id.add_campo:
                Toast.makeText(Formulario.this, "Adicionar campo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.deletar:
                AlertDialog ad = new AlertDialog.Builder(this)
                        .create();
                ad.setTitle("Realmente excluir?");
                ad.setMessage("Deseja realmente excluir o contato selecionado?");
                ad.setButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ContatoDAO dao = new ContatoDAO(Formulario.this); //Chamada o SQLITE
                        dao.deletar(contatoMostrar);
                        dao.close();
                        Intent irLista = new Intent(Formulario.this, MainActivity.class);
                        Formulario.this.startActivity(irLista);
                    }
                });
                ad.setButton2("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                ad.show();
                break;
            case R.id.editar:
                Intent irParaFormulario = new Intent(this, Formulario.class);
                contato = helper.pegaContatoDoFormulario();

                irParaFormulario.putExtra("contatoAlterar", contato);
                startActivity(irParaFormulario);

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(itemSelect);
    }
}
