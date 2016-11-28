package controllers;

import actors.ChatCluster;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import messages.NotifyAll;
import messages.RoomMessage;
import messages.UserConnection;
import models.GroupChat;
import models.GroupUser;
import models.User;
import play.PlayInternal;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.WebSocket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChatController extends Controller {

    public static Result chatGrupal(String idChat) {
        if (!userloged()) {
            return redirect(routes.LoginController.getLogin());
        }
        String creador=User.find.byId(session("user_name")).getTypeUser().getId()+"";
        List<User> users = User.users(User.find.byId(session("user_name")).getDepartment().getName());
        List<User> usersFiltered = User.listContacts(users, session("user_name"));
        List<GroupChat> mischat = GroupUser.listChats(session("user_name"));
        return ok(views.html.chatRoomGrupal.render(idChat,creador,mischat,User.find.byId(session("user_name")).getDepartment().getName(), User.find.byId(session("user_name")).getFullname(),usersFiltered));
    }

    public static Result chatDepartment(String departamento) {
        if (session("user_name") == null) {
            return redirect(routes.LoginController.getLogin());
        }
        String creador=User.find.byId(session("user_name")).getTypeUser().getId()+"";
        List<User> users = User.users(departamento);
        List<User> user = User.listContacts(User.users(), session("user_name"));

        List<User> usersFiltered = User.listContacts(users, session("user_name"));
         List<GroupChat> mischat = GroupUser.listChats(session("user_name"));
        return ok(views.html.chatRoom.render(creador,mischat, departamento, User.find.byId(session("user_name")).getFullname(), user, usersFiltered));
    }

    public static Result crearChatGrupal() {
        if (session("user_name") == null) {
            return redirect(routes.LoginController.getLogin());
        }
        if (User.find.byId(session("user_name")).getTypeUser().getId() != 2) {
            return redirect(routes.HomeController.index());
        }
        DynamicForm createChat = Form.form().bindFromRequest();
        String departamento = User.find.byId(session("user_name")).getDepartment().getName();
        List<User> users = User.users(departamento);
        List<User> user = User.listContacts(User.users(), session("user_name"));
        List<User> usersFiltered = User.listContacts(users, session("user_name"));
        List<GroupChat> mischat = GroupUser.listChats(session("user_name"));
        return ok(views.html.crearGrupo.render("2",mischat, departamento, User.find.byId(session("user_name")).getFullname(), user, usersFiltered,createChat));
    }

    public static Result chatIndividual(String departamento, String reciever) {
        if (session("user_name") == null) {
            return redirect(routes.LoginController.getLogin());
        }
       String creador=User.find.byId(session("user_name")).getTypeUser().getId()+"";
        List<User> users = User.users();

        List<User> usersFiltered = User.listContacts(users,session("user_name"));
        List<GroupChat> mischat = GroupUser.listChats(session("user_name"));
        return ok(views.html.individualChat.render(creador,mischat,departamento, User.find.byId(session("user_name")).getFullname(), usersFiltered, reciever));
    }


    public static Result crearChatNuevo() {
        DynamicForm request = Form.form().bindFromRequest();
        String name = request.get("namegroup");
        Collection<String> users =request.data().values();
        if(name.equals("")||name.isEmpty()||name==null){
            request.reject("namegroup","Nombre no puede ser vacio");
        }else{
            if(users.size()<=0){
                request.reject("inte[]","Debe seleccionar al menos un usuario");
            }else {
                GroupChat gC = new GroupChat(name);
                Ebean.save(gC);
                GroupUser gU= new GroupUser(gC,User.find.byId(session("user_name")));
                Ebean.save(gU);
                for(String u: users){
                    GroupUser g = new GroupUser(gC,User.find.byId(u));
                    Ebean.save(g);
                }
                return redirect(routes.ChatController.chatGrupal(gC.getId()+""));
            }
        }
        List<User> user = User.listContacts(User.users(), session("user_name"));
        List<User> usersFiltered = User.listContacts(User.users(User.find.byId(session("user_name")).getDepartment().getName()), session("user_name"));
        List<GroupChat> mischat = GroupUser.listChats(session("user_name"));
        return Results.badRequest(views.html.crearGrupo.render("2", mischat,User.find.byId(session("user_name")).getDepartment().getName(), User.find.byId(session("user_name")).getFullname(), user, usersFiltered,request));
    }

    public static boolean userloged(){
        if(session("user_name")==null){
            return false;
        }
        return true;
    }


    public static WebSocket<JsonNode> socketDepartmentChat(final String department, final String username) {
        return new WebSocket<JsonNode>() {

            public void onReady(WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {

                ChatCluster.userConnetion((new UserConnection(department, username, out, messages.UserConnection.ConnectionType.CONNECTED)));

                in.onMessage(new F.Callback<JsonNode>() {
                    public void invoke(JsonNode event) {
                        ChatCluster.sendMessage(new NotifyAll(new RoomMessage(department, username, event.get("text").asText())));
                    }
                });

                in.onClose(new F.Callback0() {
                    public void invoke() {
                        ChatCluster.userConnetion((new UserConnection(department, username, out, messages.UserConnection.ConnectionType.DISCONNECTED)));
                    }
                });
            }
        };
    }

    public static WebSocket<JsonNode> socketIndividualChat(final String reciever) {
        User userE = User.find.byId(session("user_name"));
        User userR = User.find.where().eq("fullname", reciever).findUnique();
        List<String> listUser = new ArrayList<>();
        listUser.add(userR.getEmail());
        listUser.add(userE.getEmail());
        Collections.sort(listUser);
        String nombres = "";
        for (String nombre : listUser) {
            nombres += nombre;
        }
        String individualRoom = nombres.replaceAll("\\s+", "").toLowerCase();
        PlayInternal.logger().info("nombre: " + individualRoom);
        return new WebSocket<JsonNode>() {
            @Override
            public void onReady(In<JsonNode> in, Out<JsonNode> out) {
                ChatCluster.userConnetion((new UserConnection(individualRoom, userE.getFullname(), out, messages.UserConnection.ConnectionType.CONNECTED)));

                in.onMessage(new F.Callback<JsonNode>() {
                    public void invoke(JsonNode event) {
                        ChatCluster.sendMessage(new NotifyAll(new RoomMessage(individualRoom, userE.getFullname(), event.get("text").asText())));
                    }
                });

                in.onClose(new F.Callback0() {
                    public void invoke() {
                        ChatCluster.userConnetion((new UserConnection(individualRoom, userE.getFullname(), out, messages.UserConnection.ConnectionType.DISCONNECTED)));
                    }
                });
            }
        };
    }

}
