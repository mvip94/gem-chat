package models;

/**
 * Created by Gabriela Mejia on 27/11/2016.
 */
public class Reporte {

    private String nombre;
    private String department;
    private String tiempo;

    public Reporte(String nombre, String department, String tiempo) {
        this.nombre = nombre;
        this.department = department;
        this.tiempo = tiempo;
    }

    public Reporte() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    @Override
    public String toString() {
        return "Reporte{" +
                "nombre='" + nombre + '\'' +
                ", department='" + department + '\'' +
                ", tiempo='" + tiempo + '\'' +
                '}';
    }
}
