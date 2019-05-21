package me.puntodigital.abmsqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.puntodigital.abmsqlite.controllers.EspeciesController;
import me.puntodigital.abmsqlite.modelos.Especie;

public class AgregarEspecieActivity extends AppCompatActivity {
    private Button btnAgregarEspecie, btnCancelarNuevaEspcie;
    private EditText etNombre, etAltura;
    private EspeciesController especiesController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_especie);

        // Instanciar vistas
        etNombre = findViewById(R.id.etNombre);
        etAltura = findViewById(R.id.etAltura);
        btnAgregarEspecie = findViewById(R.id.btnAgregarEspecie);
        btnCancelarNuevaEspcie = findViewById(R.id.btnCancelarNuevaEspecie);
        // Crear el controlador
        especiesController = new EspeciesController(AgregarEspecieActivity.this);

        // Agregar listener del botón de guardar
        btnAgregarEspecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Resetear errores a ambos
                etNombre.setError(null);
                etAltura.setError(null);
                String nombre = etNombre.getText().toString(),
                        alturaComoCadena = etAltura.getText().toString();
                if ("".equals(nombre)) {
                    etNombre.setError("Escribe el nombre de la especie");
                    etNombre.requestFocus();
                    return;
                }
                if ("".equals(alturaComoCadena)) {
                    etAltura.setError("Escribe la altura aproximada de la especie");
                    etAltura.requestFocus();
                    return;
                }

                // Ver si es un entero
                int altura;
                try {
                    altura = Integer.parseInt(etAltura.getText().toString());
                } catch (NumberFormatException e) {
                    etAltura.setError("Escribe un número entero");
                    etAltura.requestFocus();
                    return;
                }
                // Ya pasó la validación
                Especie nuevaEspecie = new Especie(nombre, altura);
                long id = especiesController.nuevaEspecie(nuevaEspecie);
                if (id == -1) {
                    // De alguna manera ocurrió un error
                    Toast.makeText(AgregarEspecieActivity.this, "Error al guardar. Intenta de nuevo", Toast.LENGTH_SHORT).show();
                } else {
                    // Terminar
                    finish();
                }
            }
        });

        // El de cancelar simplemente cierra la actividad
        btnCancelarNuevaEspcie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
