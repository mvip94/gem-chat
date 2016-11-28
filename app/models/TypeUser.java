package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class TypeUser extends Model {

    @Id
    @GeneratedValue
    private int id;

    @Constraints.Required(message = "Necesita ingresar un nombre para el tipo de usuario")
    private String name;

    public TypeUser(String name) {
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

    public static Finder<String, TypeUser> find = new Finder<>(String.class, TypeUser.class);

    @Override
    public String toString() {
        return "TypeUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
