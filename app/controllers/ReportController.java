package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Gabriela Mejia on 27/11/2016.
 */
public class ReportController extends Controller {

    public static boolean userloged(){
        if(session("user_name")==null){
            return false;
        }
        return true;
    }

    public static Result getReport(){
        if (!userloged()) {
            return redirect(routes.LoginController.getLogin());
        }
        User user = User.find.byId(session("user_name"));
        String creador=user.getTypeUser().getId()+"";
        List<User> users;
        if(user.getTypeUser().getId()==3){
            return redirect(routes.HomeController.index());
        }
        if(user.getTypeUser().getId()==1){
            users = User.users();
        }else{
            users = User.users(user.getDepartment().getName());
        }
        List<User> usersFiltered = User.listContacts(users,session("user_name"));
        List<GroupChat> mischat = GroupUser.listChats(session("user_name"));
        List<Reporte> report =new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY,0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        Calendar fin =Calendar.getInstance().getInstance();
        fin.set(Calendar.HOUR_OF_DAY,23);
        fin.set(Calendar.MINUTE,59);
        fin.set(Calendar.SECOND,59);
        Reporte r;
        for(User u:usersFiltered){
            int duracion=0;
            List<Actividad> acti = Actividad.find
                    .where().eq("user_id",u.getEmail())
                    .where().between("fecha_inicio_session",sdf.format(start.getTime()),sdf.format(fin.getTime())).findList();
            for(Actividad ac:acti){
                duracion=duracion+ac.getDuracion();
            }
            r=new Reporte();
            r.setNombre(u.getFullname());
            r.setDepartment(u.getDepartment().getName());
            r.setTiempo(convertirDuracion(duracion));
            report.add(r);
        }
        return ok(views.html.reporte.render(creador,mischat,user.getDepartment().getName(), user.getFullname(),usersFiltered,user.getDepartment().getEnterprise().getName(),report));
    }
/*

    public static void main(String[] args)
    {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        System.out.println(sdf.format(now.getTime()));
        now.set(Calendar.HOUR_OF_DAY, 0);
        System.out.println(sdf.format(now.getTime()));
    }*/

    private static String convertirDuracion(int ms)
    {
        String cadena="";
        int segs = ms%60; ms -= segs; ms /= 60;
        int mins = ms%60; ms -= mins; ms /= 60;
        int horas = ms%24;
        //HMS
        if(mins>0&&segs>0&&horas>0){
            cadena= horas+" horas "+mins+" minutos y "+segs+" segundos.";
        }
        //HM
        if(segs==0&&mins>0&&horas>0){
            cadena= horas+" horas y "+mins+" minutos.";
        }
        //HS
        if(mins==0&&segs>0&&horas>0){
            cadena= horas+" horas y "+segs+" segundos.";
        }
        //SM
        if(horas==0&&segs>0&&mins>0){
            cadena= mins+" minutos y "+segs+" segundos.";
        }
        //S
        if(horas==0&&segs>0&&mins==0){
            cadena=segs+" segundos.";
        }
        //M
        if(horas==0&&mins>0&&segs==0){
            cadena=mins+" minutos.";
        }
        //H
        if(horas>0&&mins==0&&segs==0){
            cadena=horas+" horas.";
        }
        return cadena;

    }
}
