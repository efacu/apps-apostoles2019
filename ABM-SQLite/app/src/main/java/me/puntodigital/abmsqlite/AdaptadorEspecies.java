package me.puntodigital.abmsqlite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.puntodigital.abmsqlite.modelos.Especie;

public class AdaptadorEspecies extends RecyclerView.Adapter<AdaptadorEspecies.MyViewHolder> {

    private List<Especie> listaDeEspecies;

    public void setListaDeEspecies(List<Especie> listaDeEspecies) {
        this.listaDeEspecies = listaDeEspecies;
    }

    public AdaptadorEspecies(List<Especie> especies) {
        this.listaDeEspecies = especies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View filaEspecie = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fila_epecie, viewGroup, false);
        return new MyViewHolder(filaEspecie);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        // Obtener la especie de nuestra lista gracias al índice i
        Especie especie = listaDeEspecies.get(i);

        // Obtener los datos de la lista
        String nombreEspecie = especie.getNombre();
        int alturaEspecie = especie.getAltura();
        // Y poner a los TextView los datos con setText
        myViewHolder.nombre.setText(nombreEspecie);
        myViewHolder.altura.setText(String.valueOf(alturaEspecie));
    }

    @Override
    public int getItemCount() {
        return listaDeEspecies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, altura;

        MyViewHolder(View itemView) {
            super(itemView);
            this.nombre = itemView.findViewById(R.id.tvNombre);
            this.altura = itemView.findViewById(R.id.tvAltura);
        }
    }
}
