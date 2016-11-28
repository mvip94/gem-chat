package controllers;

import com.avaje.ebean.Ebean;
import models.GroupChat;
import models.GroupUser;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.List;

public class UserController extends Controller {


    public static Result showPerfil() {
        if (session("user_name") == null) {
            return redirect(routes.LoginController.getLogin());
        }
        String creador=User.find.byId(session("user_name")).getTypeUser().getId()+"";
        DynamicForm editProfile = Form.form().bindFromRequest();
        String departamento = User.find.byId(session("user_name")).getDepartment().getName();
        List<User> users = User.users(departamento);
        List<User> user = User.listContacts(User.users(), session("user_name"));
        List<User> usersFiltered = User.listContacts(users, session("user_name"));
        List<GroupChat> mischat = GroupUser.listChats(session("user_name"));
        return ok(views.html.miPerfil.render(creador,mischat, departamento, User.find.byId(session("user_name")).getFullname(), user, usersFiltered,User.find.byId(session("user_name")),editProfile));

    }

    public static Result editarPerfil()
    {
        DynamicForm request = Form.form().bindFromRequest();
        String nombre = request.get("nombre");
        String apellido = request.get("apellido");
        String pass = request.get("password");
        String newpass = request.get("password_new");
        String conf = request.get("password_confirm");
        if(nombre.equals("")||nombre.isEmpty()||nombre==null){
            request.reject("nombre","Nombre no puede ser vacio");
        }else{
            if(apellido.equals("")||apellido.isEmpty()||apellido==null){
                request.reject("apellido","Apellido no puede ser vacio");
            }else{
                if(pass.equals("")||pass.isEmpty()||pass==null){
                    request.reject("password","Contraseña no puede ser vacia");
                }else{
                    if(!pass.equals(User.find.byId(session("user_name")).getPassword())){
                        request.reject("password","Contraseña no coincide con el registro");
                    }else{
                        if(newpass.equals("")||newpass.isEmpty()||newpass==null){
                            User u = User.find.byId(session("user_name"));
                            u.setName(nombre);
                            u.setLast(apellido);
                            u.setFullname(nombre,apellido);
                            Ebean.save(u);
                            return redirect(routes.HomeController.index());
                        }else {
                            if(conf.equals("")||conf.isEmpty()||conf==null){
                                request.reject("password_confirm","Confirmar contraseña no puede ser vacia");
                            }else{
                                if(!newpass.equals(conf)){
                                    request.reject("password_confirm","Confirmar contraseña no coincide");
                                    request.reject("password_new","Nueva contraseña no coincide");
                                }else{
                                    User u = User.find.byId(session("user_name"));
                                    u.setName(nombre);
                                    u.setLast(apellido);
                                    u.setFullname(nombre,apellido);
                                    u.setPassword(newpass);
                                    Ebean.save(u);
                                    return redirect(routes.HomeController.index());
                                }
                            }
                        }
                    }
                }
            }
        }
        String creador=User.find.byId(session("user_name")).getTypeUser().getId()+"";
        String departamento = User.find.byId(session("user_name")).getDepartment().getName();
        List<User> users = User.users(departamento);
        List<User> user = User.listContacts(User.users(), session("user_name"));
        List<User> usersFiltered = User.listContacts(users, session("user_name"));
        List<GroupChat> mischat = GroupUser.listChats(session("user_name"));
        return Results.badRequest(views.html.miPerfil.render(creador,mischat, departamento, User.find.byId(session("user_name")).getFullname(), user, usersFiltered,User.find.byId(session("user_name")),request));
    }
}
