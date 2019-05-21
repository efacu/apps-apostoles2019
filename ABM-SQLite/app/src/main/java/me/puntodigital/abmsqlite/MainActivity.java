package me.puntodigital.abmsqlite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.puntodigital.abmsqlite.controllers.EspeciesController;
import me.puntodigital.abmsqlite.modelos.Especie;

public class MainActivity extends AppCompatActivity {
    private List<Especie> listaDeEspecies;
    private RecyclerView recyclerView;
    private AdaptadorEspecies adaptadorEspecies;
    private EspeciesController especiesController;
    private FloatingActionButton fabAgregarEspecie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Ojo: este código es generado automáticamente, pone la vista y ya, pero
        // no tiene nada que ver con el código que vamos a escribir
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Lo siguiente sí es nuestro ;)
        // Definir nuestro controlador
        especiesController = new EspeciesController(MainActivity.this);

        // Instanciar vistas
        recyclerView = findViewById(R.id.recyclerViewEspecies);
        fabAgregarEspecie = findViewById(R.id.fabAgregarEspecie);


        // Por defecto es una lista vacía,
        // se la ponemos al adaptador y configuramos el recyclerView
        listaDeEspecies = new ArrayList<>();
        adaptadorEspecies = new AdaptadorEspecies(listaDeEspecies);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adaptadorEspecies);

        // Una vez que ya configuramos el RecyclerView le ponemos los datos de la BD
        refrescarListaDeEspecies();

        // Listener de los clicks en la lista, o sea el RecyclerView
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override // Un toque sencillo
            public void onClick(View view, int position) {
                // Pasar a la actividad EditarEspecieActivity.java
                Especie especieSeleccionada = listaDeEspecies.get(position);
                Intent intent = new Intent(MainActivity.this, EditarEspecieActivity.class);
                intent.putExtra("idEspecie", especieSeleccionada.getId());
                intent.putExtra("nombreEspecie", especieSeleccionada.getNombre());
                intent.putExtra("aturaEspecie", especieSeleccionada.getAltura());
                startActivity(intent);
            }

            @Override // Un toque largo
            public void onLongClick(View view, int position) {
                final Especie especieParaEliminar = listaDeEspecies.get(position);
                AlertDialog dialog = new AlertDialog
                        .Builder(MainActivity.this)
                        .setPositiveButton("Sí, eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                especiesController.eliminarEspecie(especieParaEliminar);
                                refrescarListaDeEspecies();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Confirmar")
                        .setMessage("¿Eliminar a la especie " + especieParaEliminar.getNombre() + "?")
                        .create();
                dialog.show();

            }
        }));

        // Listener del FAB
        fabAgregarEspecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simplemente cambiamos de actividad
                Intent intent = new Intent(MainActivity.this, AgregarEspecieActivity.class);
                startActivity(intent);
            }
        });

        // Créditos
        fabAgregarEspecie.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Acerca de")
                        .setMessage("CRUD de Android con SQLite ")
                        .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogo, int which) {
                                dialogo.dismiss();
                            }
                        })
                        .setPositiveButton("Sitio web", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentNavegador = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.apostoles.gov.ar"));
                                startActivity(intentNavegador);
                            }
                        })
                        .create()
                        .show();
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        refrescarListaDeEspecies();
    }

    public void refrescarListaDeEspecies() {
        /*
         * ==========
         * Justo aquí obtenemos la lista de la BD
         * y se la ponemos al RecyclerView
         * ============
         *
         * */
        if (adaptadorEspecies == null) return;
        listaDeEspecies = especiesController.obtenerEspecies();
        adaptadorEspecies.setListaDeEspecies(listaDeEspecies);
        adaptadorEspecies.notifyDataSetChanged();
    }
}
