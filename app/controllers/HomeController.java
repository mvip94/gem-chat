package controllers;


import models.GroupChat;
import models.GroupUser;
import models.User;
import play.PlayInternal;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class HomeController extends Controller {


    public static Result index() {
        String user_name = session("user_name");
        if (user_name == null) {
            DynamicForm loginForm = Form.form();
            return badRequest(views.html.login.render(loginForm,null));
        }else{
            PlayInternal.logger().info(session("user_name"));
            User user = User.find.byId(user_name);
            String creador=user.getTypeUser().getId()+"";
            List<User> users = User.users();
            List<User> usersFiltered = User.listContacts(users,session("user_name"));
            List<GroupChat> mischat = GroupUser.listChats(user_name);
            return ok(views.html.chatRoomGeneral.render(creador,mischat,user.getDepartment().getName(), user.getFullname(),usersFiltered,user.getDepartment().getEnterprise().getName()));
        }
    }


}
