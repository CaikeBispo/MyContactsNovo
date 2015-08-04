package br.com.drummond.mycontacts.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.drummond.mycontacts.About;
import br.com.drummond.mycontacts.fragments.ContatoFragment;
import br.com.drummond.mycontacts.fragments.Historico;

/**
 * Created by fabianoabreu on 04/08/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] titles={"CONTATOS","HISTÓRICO"};

    public TabsAdapter(FragmentManager fm, Context c) {
        super(fm);
        mContext=c;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;

        if (position == 0) {
            frag = new ContatoFragment();

        } else if (position == 1) {
            frag = new Historico();

            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
            ft.commit();
            mToolbar.setTitle( ((PrimaryDrawerItem) iDrawerItem).getName() );*/
        }
        Bundle b= new Bundle();
        b.putInt("position",position);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ( titles[position] );
    }
}
