package me.puntodigital.abmsqlite.modelos;

public class Especie {

    private String nombre;
    private int altura;

    private long id; // El ID de la BD

    public Especie(String nombre, int altura) {
        this.nombre = nombre;
        this.altura = altura;
    }

    // Constructor para cuando instanciamos desde la BD
    public Especie(String nombre, int altura, long id) {
        this.nombre = nombre;
        this.altura = altura;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    @Override
    public String toString() {
        return "Especie{" +
                "nombre='" + nombre + '\'' +
                ", altura=" + altura +
                '}';
    }
}
