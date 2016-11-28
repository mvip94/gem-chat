package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends Model {

    @Id
    @Constraints.Required(message = "Debe ingresar un correo")
    @Constraints.Email
    @Column(unique = true)
    private String email;

    @Constraints.Required(message = "Debe ingresar un nombre")
    private String name;
    @Constraints.Required(message = "Debe ingresar un apellido")
    private String last;

    private String fullname;

    @Constraints.Required(message = "Debe ingresar una contraseña")
    @JsonIgnore
    @Constraints.MinLength(message = "Caracteres minimos 8", value = 8)
    private String password;

    @OneToOne()
    @JoinColumn(name = "id_typeuser")
    private TypeUser typeUser;

    @OneToOne()
    @JoinColumn(name = "id_department")
    private Department department;

    public User(String email, String name,String last, String password, TypeUser typeUser, Department department) {
        this.email = email;
        this.name = name;
        this.last = last;
        this.fullname = name+" "+last;
        this.password = password;
        this.typeUser=typeUser;
        this.department = department;
    }

    public static List<User> users(){
        List<User> users = User.find.all();
        return users;
    }

    public static List<User> users(String departamento) {
        Department department = Department.find.where().eq("name", departamento).findUnique();
        List<User> users = User.find.where().eq("id_department", department.getId()).findList();
        return users;
    }

    public static List<User> listContacts(List<User> usuarios,String email){
        List<User> usersFiltered = new ArrayList<>();
        for (User user : usuarios){
            if(!user.getEmail().equals(email)){
                usersFiltered.add(user);
            }
        }
        return usersFiltered;
    }


    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (User.byEmail(email) != null) {
            errors.add(new ValidationError("email", "Este Correo ya esta registrado."));
        }
        return errors.isEmpty() ? null : errors;
    }

    private static Object byEmail(String email) {
        return User.find.byId(email);
    }

    public static Finder<String, User> find = new Finder<>(String.class, User.class);

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String name,String last) {
        this.fullname = name+" "+last;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public TypeUser getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", last='" + last + '\'' +
                ", fullname='" + fullname + '\'' +
                ", password='" + password + '\'' +
                ", typeUser=" + typeUser +
                ", department=" + department +
                '}';
    }
}
