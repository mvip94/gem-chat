package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import models.Message;
import models.User;
import play.Play;
import play.PlayInternal;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageController extends Controller {

    public static Result getMessages(String roomchat,String chatType) {
        if(chatType.equals("individual")){
            PlayInternal.logger().info("session activa: "+session("user_name"));
            User userE = User.find.byId(session("user_name"));
            User userR = User.find.where().eq("fullname", roomchat).findUnique();
            List<String> listUser = new ArrayList<>();
            listUser.add(userR.getEmail());
            listUser.add(userE.getEmail());
            Collections.sort(listUser);
            String nombres = "";
            //cesar@martinez.com
            for (String nombre : listUser) {
                nombres += nombre;
            }
            String individualRoom = nombres.replaceAll("\\s+", "").toLowerCase();
            PlayInternal.logger().info("nombre del individual: "+individualRoom);
            List<Message> messages = Message.find.setMaxRows(20).orderBy("fecha_envio desc").where().eq("room", individualRoom).findList();
            List<Message> reverse = Lists.reverse(messages);
            JsonNode jsonNode = Json.toJson(reverse);
            return ok(jsonNode);
        }
        List<Message> messages = Message.find.setMaxRows(20).orderBy("fecha_envio asc").where().eq("room", roomchat).findList();
        List<Message> reverse = Lists.reverse(messages);
        JsonNode jsonNode = Json.toJson(Lists.reverse(reverse));
        return ok(jsonNode);
    }

}
