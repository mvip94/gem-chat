package models;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Message extends Model{

    @Id
    @GeneratedValue
    private int id;
    private final String message;
    private final String sender;
    @JsonIgnore
    private final String room;
    private final String fechaEnvio;

    public Message(String message, String sender, String room, String fechaEnvio) {
        this.message = message;
        this.sender = sender;
        this.room = room;
        this.fechaEnvio = fechaEnvio;
    }

    public static Finder<Integer,Message> find = new Finder<>(Integer.class,Message.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getRoom() {
        return room;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }
}
