package com.example.app_garbager_iot.Adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_garbager_iot.Model.Container;
import com.example.app_garbager_iot.Model.PersonModel;
import com.example.app_garbager_iot.R;
import com.example.app_garbager_iot.utils.MoreUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.ContainerViewHolder>  {
    private List<Container> data = new ArrayList<>();
    private  List<Container> originalData = new ArrayList<>();
    private Context context;
    public ContainerAdapter(){

    }
   public void setData(List<Container>data){
        this.data = data;
        this.originalData.addAll(data);
        notifyDataSetChanged();
   }

    @NonNull
    @Override
    public ContainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ContainerViewHolder(LayoutInflater.from(context).inflate(R.layout.carview_container, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ContainerViewHolder holder, int position) {
        Container container = data.get(position);

        holder.txtname.setText(MoreUtils.coalesce(container.getNameContainer(), "N/D"));
        holder.txtAdrres.setText(MoreUtils.coalesce(container.getAddress(), "N/D"));
        holder.txtState.setText(MoreUtils.coalesce(container.getState(), "N/D"));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putSerializable("container",new Gson().toJson(data.get(holder.getAdapterPosition())));

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void filter(String strSearch){

        if (strSearch.length() == 0){

            this.data.clear();
            this.data.addAll(this.originalData);

        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                List<Container> collect = this.originalData.stream()
                        .filter(p -> p.getNameContainer().toLowerCase().contains(strSearch.toLowerCase()))
                        .collect(Collectors.toList());
                this.data.clear();
                data.addAll(collect);
            }
            else {
                for (Container p: originalData){
                    if (p.getNameContainer().toLowerCase().contains(strSearch.toLowerCase())){
                        data.add(p);
                    }
                }

            }
        }
        notifyDataSetChanged();
    }

    public class ContainerViewHolder extends RecyclerView.ViewHolder {

        TextView txtname;
        TextView txtAdrres;
        TextView txtState;
        CardView cardView;


        public ContainerViewHolder(@NonNull View itemView) {
            super(itemView);

            txtname = itemView.findViewById(R.id.txtNameContainer);
            txtAdrres = itemView.findViewById(R.id.txtAddres);
            txtState = itemView.findViewById(R.id.txtStateContent);
            cardView = itemView.findViewById(R.id.cardViewContainer);
        }


    }
}
