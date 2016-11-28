package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class GroupUser extends Model {


    @Id
    @GeneratedValue
    private int id;

    @OneToOne()
    @JoinColumn(name = "id_group")
    private GroupChat groupChat;

    @OneToOne()
    @JoinColumn(name = "id_user")
    private User user;

    public GroupUser(GroupChat groupChat, User user) {
        this.groupChat = groupChat;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroupChat getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public static Finder<String, GroupUser> find = new Finder<>(String.class, GroupUser.class);

    public static List<GroupChat> listChats(String email) {
        User user = User.find.where().eq("email",email).findUnique();
        List<GroupUser> groupsChat = GroupUser.find.where().eq("id_user", user.getEmail()).findList();
        List<GroupChat> groups = new ArrayList<>();
        for (GroupUser gU : groupsChat){
            GroupChat g = GroupChat.find.byId(gU.getGroupChat().getId()+"");
            groups.add(g);
        }
        return groups;
    }
}

