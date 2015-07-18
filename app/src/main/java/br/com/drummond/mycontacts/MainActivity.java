package br.com.drummond.mycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;

import java.util.ArrayList;
import java.util.List;

import br.com.drummond.mycontacts.fragments.ContatoFragment;
import br.com.drummond.mycontacts.fragments.Historico;
import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;


public class MainActivity extends ActionBarActivity {
    private static String TAG = "LOG";
    private Toolbar mToolbar;
    private Drawer.Result navigationDrawerLeft;
    private AccountHeader.Result headerNavigationLeft;
    private int mPositionClicked;
    private int mItemDrawerSelected;

    private OnCheckedChangeListener OnCheckedChangeListener=new OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
            Toast.makeText(MainActivity.this,"onCheckedChanged: "+(b? "true":"false"),Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if(savedInstanceState != null){
            mItemDrawerSelected = savedInstanceState.getInt("mItemDrawerSelected", 0);
        }*/

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("MyContacts");
        mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(mToolbar);


        // FRAGMENT
        ContatoFragment frag = (ContatoFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if(frag == null) {
            frag = new ContatoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
            ft.commit();
        }
        /*CarFragment frag = (CarFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if(frag == null) {
            frag = new CarFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
            ft.commit();
        }*/

        /*Historico frag = (Historico) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if(frag == null) {
            frag = new Historico();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
            ft.commit();
        }*/

        //HEADER NAVIGATION DRAWER
        headerNavigationLeft= new AccountHeader()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(false)
                .withHeaderBackground(R.drawable.camaro)
                .addProfiles(
                        new ProfileDrawerItem().withName("Fabiano de Lima").withEmail("teste1@teste.com.br").withIcon(getResources().getDrawable(R.drawable.person_1))
                )
                /*.withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile iProfile, boolean b) {
                        Toast.makeText(MainActivity.this,"onProfileChanged: "+iProfile.getName(),Toast.LENGTH_LONG).show();
                        headerNavigationLeft.setBackgroundRes(R.drawable.vyron);
                        return false;
                    }
                })*/
                .build();

        //NAVIGATION DRAWER LEFT
        navigationDrawerLeft= new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSavedInstance(savedInstanceState)
                .withDrawerGravity(Gravity.LEFT)
                .withSelectedItem(-1)
                .withAccountHeader(headerNavigationLeft)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Fragment frag = null;
                        mItemDrawerSelected = i;

                        if (i == 0) { // ALL CARS
                            //frag = new CarFragment();
                            Intent irSobre = new Intent(MainActivity.this, About.class);

                            startActivity(irSobre);
                        } else if (i == 1) { // LUXURY CAR
                            frag = new Historico();

                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
                            ft.commit();
                            mToolbar.setTitle( ((PrimaryDrawerItem) iDrawerItem).getName() );
                        }

                        for (int count = 0, tam = navigationDrawerLeft.getDrawerItems().size(); count < tam; count++) {
                            if (count == mPositionClicked && mPositionClicked <= 2) {
                                PrimaryDrawerItem aux = (PrimaryDrawerItem) navigationDrawerLeft.getDrawerItems().get(count);
                                aux.setIcon(getResources().getDrawable( getCorretcDrawerIcon( count, false ) ));
                                break;
                            }
                        }

                        if(i <= 3){
                            ((PrimaryDrawerItem) iDrawerItem).setIcon(getResources().getDrawable( getCorretcDrawerIcon( i, true ) ));
                        }

                        mPositionClicked = i;
                        navigationDrawerLeft.getAdapter().notifyDataSetChanged();
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Toast.makeText(MainActivity.this, "onItemLongClick: " + i, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .build();

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Sobre").withIcon(getResources().getDrawable(R.drawable.car_1)));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Grupos"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Grupo1").withIcon(getResources().getDrawable(R.drawable.car_1)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Grupo2").withIcon(getResources().getDrawable(R.drawable.car_1)));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
        navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Notificação").withChecked(true).withOnCheckedChangeListener(OnCheckedChangeListener));
        navigationDrawerLeft.addItem(new ToggleDrawerItem().withName("News").withChecked(true).withOnCheckedChangeListener(OnCheckedChangeListener));
        navigationDrawerLeft.addItem(new DividerDrawerItem());
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Avalie").withIcon(getResources().getDrawable(R.drawable.car_1)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Backup").withIcon(getResources().getDrawable(R.drawable.car_1)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Contato").withIcon(getResources().getDrawable(R.drawable.car_1)));
    }


    private int getCorretcDrawerIcon(int position, boolean isSelecetd){
        switch(position){
            case 0:
                return( isSelecetd ? R.drawable.car_selected_1 : R.drawable.car_1 );
            case 1:
                return( isSelecetd ? R.drawable.car_selected_2 : R.drawable.car_2 );
            case 2:
                return( isSelecetd ? R.drawable.car_selected_3 : R.drawable.car_3 );
            case 3:
                return( isSelecetd ? R.drawable.car_selected_4 : R.drawable.car_4 );
        }
        return(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.novo){
            startActivity(new Intent(this, Formulario.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /*public List<Car> getSetCarList(int qtd){
        String[] models = new String[]{"Gallardo", "Vyron", "Corvette", "Pagani Zonda", "Porsche 911 Carrera", "BMW 720i", "DB77", "Mustang", "Camaro", "CT6"};
        String[] brands = new String[]{"Lamborghini", " bugatti", "Chevrolet", "Pagani", "Porsche", "BMW", "Aston Martin", "Ford", "Chevrolet", "Cadillac"};
        int[] photos = new int[]{R.drawable.gallardo, R.drawable.vyron, R.drawable.corvette, R.drawable.paganni_zonda, R.drawable.porsche_911, R.drawable.bmw_720, R.drawable.db77, R.drawable.mustang, R.drawable.camaro, R.drawable.ct6};
        String description = "Lorem Ipsum é simplesmente uma simulação de texto da indústria tipográfica e de impressos, e vem sendo utilizado desde o século XVI, quando um impressor desconhecido pegou uma bandeja de tipos e os embaralhou para fazer um livro de modelos de tipos. Lorem Ipsum sobreviveu não só a cinco séculos, como também ao salto para a editoração eletrônica, permanecendo essencialmente inalterado. Se popularizou na década de 60, quando a Letraset lançou decalques contendo passagens de Lorem Ipsum, e mais recentemente quando passou a ser integrado a softwares de editoração eletrônica como Aldus PageMaker.";
        List<Car> listAux = new ArrayList<>();

        for(int i = 0; i < qtd; i++){
            Car c = new Car( models[i % models.length], brands[ i % brands.length], photos[i % models.length] );
            c.setDescription( description );
            c.setTel("33221155");

            listAux.add(c);
        }
        return(listAux);
    }*/
}
