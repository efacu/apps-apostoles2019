package me.puntodigital.abmsqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.puntodigital.abmsqlite.controllers.EspeciesController;
import me.puntodigital.abmsqlite.modelos.Especie;

public class EditarEspecieActivity extends AppCompatActivity {
    private EditText etEditarNombre, etEditarAltura;
    private Button btnGuardarCambios, btnCancelarEdicion;
    private Especie especie;//La especie que vamos a estar editando
    private EspeciesController especiesController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_especie);

        // Recuperar datos que enviaron
        Bundle extras = getIntent().getExtras();
        // Si no hay datos salimos
        if (extras == null) {
            finish();
            return;
        }
        // Instanciar el controlador de las espcies
        especiesController = new EspeciesController(EditarEspecieActivity.this);

        // Rearmar la especie
        // Nota: igualmente solamente podríamos mandar el id y recuperar la especie de la BD
        long idEspecie = extras.getLong("idEspecie");
        String nombreEspecie = extras.getString("nombreEspecie");
        int alturaEspecie = extras.getInt("alturaEspecie");
        especie = new Especie(nombreEspecie, alturaEspecie, idEspecie);


        // Ahora declaramos las vistas
        etEditarAltura = findViewById(R.id.etEditarAltura);
        etEditarNombre = findViewById(R.id.etEditarNombre);
        btnCancelarEdicion = findViewById(R.id.btnCancelarEdicionEspecie);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambiosEspecie);


        // Rellenar los EditText con los datos de la especie
        etEditarAltura.setText(String.valueOf(especie.getAltura()));
        etEditarNombre.setText(especie.getNombre());

        // Listener del click del botón para salir, simplemente cierra la actividad
        btnCancelarEdicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Listener del click del botón que guarda cambios
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remover previos errores si existen
                etEditarNombre.setError(null);
                etEditarAltura.setError(null);
                // Crear la especie con los nuevos cambios pero ponerle
                // el id de la anterior
                String nuevoNombre = etEditarNombre.getText().toString();
                String posibleNuevaAltura = etEditarAltura.getText().toString();
                if (nuevoNombre.isEmpty()) {
                    etEditarNombre.setError("Escribe el nombre");
                    etEditarNombre.requestFocus();
                    return;
                }
                if (posibleNuevaAltura.isEmpty()) {
                    etEditarAltura.setError("Escribe la altura");
                    etEditarAltura.requestFocus();
                    return;
                }
                // Si no es entero, igualmente marcar error
                int nuevaAltura;
                try {
                    nuevaAltura = Integer.parseInt(posibleNuevaAltura);
                } catch (NumberFormatException e) {
                    etEditarAltura.setError("Escribe un número");
                    etEditarAltura.requestFocus();
                    return;
                }
                // Si llegamos hasta aquí es porque los datos ya están validados
                Especie especieConNuevosCambios = new Especie(nuevoNombre, nuevaAltura, especie.getId());
                int filasModificadas = especiesController.guardarCambios(especieConNuevosCambios);
                if (filasModificadas != 1) {
                    // De alguna forma ocurrió un error porque se debió modificar únicamente una fila
                    Toast.makeText(EditarEspecieActivity.this, "Error guardando cambios. Intente de nuevo.", Toast.LENGTH_SHORT).show();
                } else {
                    // Si las cosas van bien, volvemos a la principal
                    // cerrando esta actividad
                    finish();
                }
            }
        });
    }
}
