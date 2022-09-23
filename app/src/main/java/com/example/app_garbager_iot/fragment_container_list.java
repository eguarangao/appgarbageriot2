package com.example.app_garbager_iot;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app_garbager_iot.Adapters.ContainerAdapter;
import com.example.app_garbager_iot.Model.Container;
import com.example.app_garbager_iot.Model.PersonModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class fragment_container_list extends Fragment implements SearchView.OnQueryTextListener {

    FloatingActionButton favNewContainer;
   String responseSystem;
   Container container = new Container();
    private RecyclerView recyclerView;
    private android.widget.SearchView svSearchPatient;
    private ContainerAdapter containerAdapter;
    public fragment_container_list() {
     super(R.layout.fragment_container_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_container_list, container, false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}