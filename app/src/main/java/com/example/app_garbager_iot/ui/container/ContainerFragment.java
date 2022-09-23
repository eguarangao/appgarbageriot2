package com.example.app_garbager_iot.ui.container;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_garbager_iot.Adapters.ContainerAdapter;
import com.example.app_garbager_iot.MainActivity;
import com.example.app_garbager_iot.Model.Container;
import com.example.app_garbager_iot.Model.PersonModel;
import com.example.app_garbager_iot.R;
import com.example.app_garbager_iot.Retrofit.FullApis;
import com.example.app_garbager_iot.databinding.FragmentContainerBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContainerFragment extends Fragment implements SearchView.OnQueryTextListener {

    FloatingActionButton favNewContainer;
    String cadenaRespons;
    PersonModel personlog = new PersonModel();

    private RecyclerView recyclerView;
    private SearchView svSearchView;
    private ContainerAdapter containerAdapter;
    private FragmentContainerBinding binding;
    private int id_person;
    private PersonModel personModel;
    public ContainerFragment(){
        super(R.layout.fragment_container_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments()!=null){
            id_person = getArguments().getInt("id_person",0);
            personModel = getArguments().getParcelable("p");

        }

        recyclerView = view.findViewById(R.id.reciclerViewContainer);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        containerAdapter = new ContainerAdapter();
        recyclerView.setAdapter(containerAdapter);

        svSearchView = view.findViewById(R.id.txtSearchContainer);
        ((MainActivity)getActivity()).loadData();

        personlog =  ((MainActivity)getActivity()).loadData();

   try{
       getContainer();
   }catch (Exception e){
       e.printStackTrace();
   }
   initLstener();
    }

    public void getContainer()throws  Exception{
        String id = ""+ personlog.getId();
        Call<List<Container>> containerList = FullApis.getContainerServices().getByFindContainer(id);
         containerList.enqueue(new Callback<List<Container>>() {
             @Override
             public void onResponse(Call<List<Container>> call, Response<List<Container>> response) {
                 if(response.isSuccessful()){
                        List<Container> containers = response.body();
                        containerAdapter.setData(containers);
                 }
             }

             @Override
             public void onFailure(Call<List<Container>> call, Throwable t) {
                 Log.e("faliure", t.getLocalizedMessage());
             }
         });
    }

    private void initLstener(){
        svSearchView.setOnQueryTextListener(this);
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        containerAdapter.filter(newText);
        return false;
    }
}