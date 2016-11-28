package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;


@Entity
public class Department extends Model {

    @Id
    @GeneratedValue
    private int id;

    @Constraints.Required(message = "Necesita ingresar un nombre para el departamento")
    private String name;

    @OneToOne()
    @JoinColumn(name = "id_enterprise")
    private Enterprise enterprise;

    public Department(String name, Enterprise enterprise) {
        this.name = name;
        this.enterprise=enterprise;
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

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public static Finder<String, Department> find = new Finder<>(String.class, Department.class);
}
