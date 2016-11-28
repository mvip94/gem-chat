package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;


@Entity
public class Enterprise extends Model {

    @Id
    @GeneratedValue
    private int id;

    @Constraints.Required(message = "Necesita ingresar un nombre para la empresa")
    private String name;

    public Enterprise(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Finder<String, Enterprise> find = new Finder<>(String.class, Enterprise.class);

    @Override
    public String toString() {
        return "Enterprise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
