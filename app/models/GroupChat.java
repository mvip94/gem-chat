package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;


@Entity
public class GroupChat extends Model {

    @Id
    @GeneratedValue
    private int id;

    @Constraints.Required(message = "Necesita ingresar un nombre para el chat grupal")
    private String name;

    public GroupChat(String name) {
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

    public static Finder<String, GroupChat> find = new Finder<>(String.class, GroupChat.class);

    @Override
    public String toString() {
        return "GroupChat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
