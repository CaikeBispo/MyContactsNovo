package br.com.drummond.mycontacts;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import br.com.drummond.mycontacts.adapter.TabsAdapter;
import br.com.drummond.mycontacts.externals.SlidingTabLayout;
import br.com.drummond.mycontacts.fragments.ContatoFragment;
import br.com.drummond.mycontacts.fragments.Historico;
import br.com.drummond.mycontacts.lista.dao.ContatoDAO;
import br.com.drummond.mycontacts.lista.dao.UserDAO;
import br.com.drummond.mycontacts.lista.modelo.Contato;
import br.com.drummond.mycontacts.lista.modelo.User;
import br.com.drummond.mycontacts.mapa.AtualizadorDePosicao;
import br.com.drummond.mycontacts.notification.NotificationUtils;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;


public class MainActivity extends ActionBarActivity{
    private static String TAG = "LOG";
    private Toolbar mToolbar;
    private Drawer.Result navigationDrawerLeft;
    private AccountHeader.Result headerNavigationLeft;
    private int mPositionClicked;
    private int mItemDrawerSelected;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    public static String operator;

    //trabalhando com mapas
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private AtualizadorDePosicao atualizador;

    public static final String PREF_NAME="mycontacts";
    public Boolean vibrate;
    public Boolean notifications;

    private OnCheckedChangeListener OnCheckedChangeListener=new OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
            //Obtem a instancia de shared preferences
            SharedPreferences prefs=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
            SharedPreferences.Editor editor=prefs.edit();
            setNotifications(b);
            editor.putBoolean("notifications",b);
            editor.commit();
        }
    };

    private OnCheckedChangeListener OnCheckedChangeListenerVibrate=new OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
            //Obtem a instancia de shared preferences
            SharedPreferences prefs=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
            SharedPreferences.Editor editor=prefs.edit();
            setVibrate(b);
            editor.putBoolean("vibrate", b);
            editor.commit();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DescobriMinhaOperadora();
        atualizador = new AtualizadorDePosicao(MainActivity.this); //Pegando nossa localizacao atual

        /*if(savedInstanceState != null){
            mItemDrawerSelected = savedInstanceState.getInt("mItemDrawerSelected", 0);
        }*/

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("MyContacts");
        //mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(mToolbar);

        //Obtem a instancia de shared preferences
        SharedPreferences prefs=getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        setVibrate(prefs.getBoolean("vibrate", false));
        setNotifications(prefs.getBoolean("notifications", false));


        //TABS
        mViewPager= (ViewPager) findViewById(R.id.vp_tabs);
        mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(),this));

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.slt_tabs);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.white));
        mSlidingTabLayout.setViewPager(mViewPager);

        //vars of autenticacao.java
        Intent intent = getIntent();
        //if(getIntent().hasExtra()){}
        String nomeCa = Autenticacao.teste;

        final UserDAO db = new UserDAO(this);
        List<User> list = db.getUser(nomeCa);
        final User user = list.get(0);
        String name = user.getName();
        String lastName = user.getLastName();
        String mail = user.getEmail();
        String foto = user.getFoto();
        Log.i("getUser() Foto main -> ", user.getFoto());
        //HEADER NAVIGATION DRAWER
        headerNavigationLeft= new AccountHeader()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(false)
                .withHeaderBackground(R.drawable.fundo)
                .addProfiles(
                        new ProfileDrawerItem().withName(name+" "+lastName).withEmail(mail).withIcon(getResources().getDrawable(R.drawable.person_1))
                )
                .build();
        Log.i("foto",user.getFoto());


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

                        switch (i){
                            case 0:
                                Intent irSobre = new Intent(MainActivity.this, About.class);
                                startActivity(irSobre);
                                break;
                            case 5:
                                Toast.makeText(MainActivity.this, "Backup de contatos",Toast.LENGTH_SHORT).show();
                                break;
                            case 6:
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("message/rfc822");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@mycontacts.com.br"});
                                try {
                                    MainActivity.this.startActivity(Intent.createChooser(intent, "Enviar email com: "));
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, "O contato não possui email cadastrado!",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(MainActivity.this,"Algum erro ocorreu!!! "+i,Toast.LENGTH_SHORT).show();
                        }

                        for (int count = 0, tam = navigationDrawerLeft.getDrawerItems().size(); count < tam; count++) {
                            //Para trocar a cor do icone do item de menu do NavigationDrawer ao ser clicado
                            /*if (count == mPositionClicked && mPositionClicked <= 2) {
                                PrimaryDrawerItem aux = (PrimaryDrawerItem) navigationDrawerLeft.getDrawerItems().get(count);
                                aux.setIcon(getResources().getDrawable( getCorretcDrawerIcon( count, false ) ));
                                break;
                            }*/
                            if (count == mPositionClicked) {
                                PrimaryDrawerItem aux = (PrimaryDrawerItem) navigationDrawerLeft.getDrawerItems().get(count);
                                aux.setIcon(getResources().getDrawable( getCorretcDrawerIcon( count, false ) ));
                                break;
                            }
                        }

                        if(i <= 3){
                            ((PrimaryDrawerItem) iDrawerItem).setIcon(getResources().getDrawable( getCorretcDrawerIcon( i, true ) ));
                        }

                        ((PrimaryDrawerItem) iDrawerItem).setIcon(getResources().getDrawable( getCorretcDrawerIcon( i, true ) ));

                        mPositionClicked = i;
                        //navigationDrawerLeft.getAdapter().notifyDataSetChanged();
                    }
                })
                .build();

        //Abaixo criando os itens do menu do NAVIGATION DRAWER
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Sobre").withIcon(getResources().getDrawable(R.drawable.ic_nav_about)));
        /*navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Grupos"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Grupo1").withIcon(getResources().getDrawable(R.drawable.ic_nav_group)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Grupo2").withIcon(getResources().getDrawable(R.drawable.ic_nav_group)));*/

        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
        navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Notificação").withChecked(getNotifications()).withOnCheckedChangeListener(OnCheckedChangeListener));
        navigationDrawerLeft.addItem(new ToggleDrawerItem().withName("Vibrar").withChecked(getVibrate()).withOnCheckedChangeListener(OnCheckedChangeListenerVibrate));

        navigationDrawerLeft.addItem(new DividerDrawerItem());
        //navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Avalie").withIcon(getResources().getDrawable(R.drawable.ic_nav_avalie)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Backup").withIcon(getResources().getDrawable(R.drawable.ic_nav_cloud_upload)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Contato").withIcon(getResources().getDrawable(R.drawable.email)));
    }

    public void DescobriMinhaOperadora() {
        //LIGAÇÃO EFETUADA NO FRAGMENT1
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        operator = tm.getSimOperatorName();
    }


    private int getCorretcDrawerIcon(int position, boolean isSelecetd){
        switch(position){
            case 0:
                return( isSelecetd ? R.drawable.ic_nav_about_selected : R.drawable.ic_nav_about );
            /*case 1:
              return( isSelecetd ? R.drawable.ic_nav_group_selected : R.drawable.ic_nav_group );
            case 2:
                return( isSelecetd ? R.drawable.ic_nav_group_selected : R.drawable.ic_nav_group );
            case 3:
                return( isSelecetd ? R.drawable.ic_nav_avalie_disabled : R.drawable.ic_nav_avalie );*/
            case 5:
                return( isSelecetd ? R.drawable.ic_nav_cloud_upload_selected : R.drawable.ic_nav_cloud_upload );
            case 6:
                return( isSelecetd ? R.drawable.email_selected : R.drawable.email );
        }
        return(0);
    }

    public Boolean getVibrate() {
        return vibrate;
    }

    public void setVibrate(Boolean vibrate) {
        this.vibrate = vibrate;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"FECHA",Toast.LENGTH_SHORT).show();
        atualizador.cancelar();
    }*/
}
