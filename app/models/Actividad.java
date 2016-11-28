package models;

import com.avaje.ebean.Ebean;
import play.PlayInternal;
import play.db.ebean.Model;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Actividad extends Model {

    @Id
    @GeneratedValue
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Timestamp fecha_inicio_session;
    @Nullable
    private Timestamp fecha_fin_session;
    @Nullable
    private int duracion;

    public Actividad(User user, Timestamp fecha_inicio_session) {
        this.user = user;
        this.fecha_inicio_session = fecha_inicio_session;
    }

    public static Actividad registerSessionInit(String email) {
        User user = User.find.byId(email);
        java.util.Date date = new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        Actividad actividad = new Actividad(user, timestamp);
        Ebean.save(actividad);
        return actividad;
    }

    public static void updateData(String id_actividad) {
        Actividad actividad = Actividad.find.byId(Integer.parseInt(id_actividad));
        Timestamp fecha_inicio_session = actividad.getFecha_inicio_session();
        Timestamp fecha_fin_session = new Timestamp(new Date().getTime());
        actividad.setFecha_fin_session(fecha_fin_session);
        int diferencia = actividad.calculateDiff(fecha_inicio_session, fecha_fin_session);
        actividad.setDuracion(diferencia);
        Ebean.update(actividad);
    }

    private int calculateDiff(Timestamp fecha_inicio, Timestamp fecha_fin_session) {

        long milliseconds = fecha_fin_session.getTime() - fecha_inicio.getTime();

        int seconds = (int) milliseconds / 1000;

        return seconds;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getFecha_inicio_session() {
        return fecha_inicio_session;
    }

    public void setFecha_inicio_session(Timestamp fecha_inicio_session) {
        this.fecha_inicio_session = fecha_inicio_session;
    }

    @Nullable
    public Timestamp getFecha_fin_session() {
        return fecha_fin_session;
    }

    public void setFecha_fin_session(@Nullable Timestamp fecha_fin_session) {
        this.fecha_fin_session = fecha_fin_session;
    }

    @Nullable
    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(@Nullable int duracion) {
        this.duracion = duracion;
    }

    public static Finder<Integer, Actividad> find = new Finder<>(Integer.class, Actividad.class);
}
