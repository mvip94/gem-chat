import actors.ChatCluster;
import akka.actor.Props;

import com.avaje.ebean.Ebean;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;  
import com.google.inject.Injector;

import models.Department;
import models.Enterprise;
import models.TypeUser;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application application) {
        ChatCluster.CHAT_CLUSTER = Akka.system().actorOf(Props.create(ChatCluster.class), "ChatCluster");
        
         
        
    }

    void initData(){
        Enterprise enterprise = new Enterprise("Google");
        Enterprise enterprise1 = new Enterprise("GEMCorp");
        List<Enterprise> entreprises = new ArrayList<>();
        entreprises.add(enterprise);
        entreprises.add(enterprise1);

        Ebean.save(entreprises);

        Department department = new Department("Informatica",enterprise);
        Department department2 = new Department("BI",enterprise1);
        List<Department> departments = new ArrayList<>();
        departments.add(department);
        departments.add(department2);

        Ebean.save(departments);

        TypeUser typeUser = new TypeUser("CEO");
        TypeUser typeUser1 = new TypeUser("Jefe");
        TypeUser typeUser2 = new TypeUser("Empleado");
        List<TypeUser> types = new ArrayList<>();
        types.add(typeUser);
        types.add(typeUser1);
        types.add(typeUser2);

        Ebean.save(types);

        User user1 = new User(
                "cesar@martinez.com",
                "Cesar",
                "Martinez",
                "1234",
                typeUser,
                department);
        User user2 = new User(
                "mario@vides.com",
                "Mario",
                "Vides",
                "1234",
                typeUser1,department);
        User user7 = new User(
                "sharyl@mendoza.com",
                "Sharyl",
                "Mendoza",
                "1234",
                typeUser2,department);
        User user3 = new User(
                "gaby@mejia.com",
                "Gabriela",
                "Mejia",
                "1234",
                typeUser1,department);
        User user4 = new User(
                "samuel@zepeda.com",
                "Samuel",
                "Zepeda",
                "1234",
                typeUser2,department2);
        User user5 = new User(
                "marcela@lopez.com",
                "Marcela",
                "Lopez",
                "1234",
                typeUser2,department2);
        User user6 = new User(
                "elissa@sanchez.com",
                "Elissa",
                "Sanchez",
                "1234",
                typeUser2,department2);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);

        Ebean.save(users);

    }

}
