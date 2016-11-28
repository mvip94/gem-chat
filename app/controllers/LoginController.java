package controllers;

import com.avaje.ebean.Ebean;
import models.Actividad;
import models.User;
import play.PlayInternal;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.sql.Timestamp;
import java.text.ParseException;

public class LoginController extends Controller{

    public static Result getLogin(){
        String correo = session("user_name");
        if(correo == null){
            DynamicForm loginForm = Form.form().bindFromRequest();
            return badRequest(views.html.login.render(loginForm,null));
        }else{
            return redirect(routes.HomeController.index());
        }
    }

    public static Result getLogout() throws ParseException {
        Actividad.updateData(session("actividad"));
        session().clear();
        return redirect(routes.LoginController.getLogin());
    }


    public static Result postLogin() throws ParseException {
        DynamicForm request = Form.form().bindFromRequest();
        String email = request.get("email");
        String password = request.get("password");
        if(Login.logUser(email,password)){
            Actividad actividad = Actividad.registerSessionInit(email);
            session("actividad", String.valueOf(actividad.getId()));
            return redirect(routes.HomeController.index());
        }else{
            request.reject("email","La informacion proporcionada no esta en nuestros registros");
        }
        return Results.badRequest(views.html.login.render(request,null));
    }

    private static class Login{
        String email;
        String password;

        public Login(String email, String password) {
            this.email = email;
            this.password = password;
        }

        static boolean authenticateEmail(String email){
            PlayInternal.logger().info("email: "+email);
            User user = User.find.byId(email);
            return user != null;
        }

        static boolean authenticatingUser(String email, String password){
            User user = User.find.byId(email);
            if(user != null){
                if(user.getEmail().equals(email) && user.getPassword().equals(password)){
                    return true;
                }
            }
            return false;
        }

        static boolean logUser(String email, String password){
            boolean email_exists = authenticateEmail(email);

            if(!email_exists){
                return false;
            }else{
                boolean autenticated = authenticatingUser(email,password);
                if(! autenticated){
                    return false;
                }else{
                    session().clear();
                    session("user_name",email);
                    return true;
                }
            }
        }

        @Override
        public String toString() {
            return "Login{" +
                    "email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
