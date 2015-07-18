package br.com.drummond.mycontacts.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.drummond.mycontacts.MainActivity;
import br.com.drummond.mycontacts.R;

/**
 * Created by Fabiano de Lima on 09/07/2015.
 */
public class Historico extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico, container, false);
        return view;
    }
}
