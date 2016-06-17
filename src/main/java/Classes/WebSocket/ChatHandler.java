package Classes.WebSocket;

import Classes.jpaIntegration.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by MEUrena on 6/13/16.
 * All rights reserved.
 */

public class ChatHandler {

    public static List<Session> connectedUsers = new ArrayList<>();
    public static List<UserInfo> userInfos = new ArrayList<>();

    private static GsonBuilder gsonBuilder = new GsonBuilder();
    private static Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

    public static void broadcastMessage(String sender, String message) throws Exception {
        try {
            for (Session user : connectedUsers) {
                user.getRemote().sendString("Sender: " + sender + "Message: " + message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public static void sendMessage(Message message){
        try{
            switch (message.getType()){
                case USER_JOINED:
                case USER_LEFT:
                case ADMIN_MESSAGE:
                    //send to all admins:
                    boolean adminPresent = false;
                    for(UserInfo userInfo : userInfos) {
                        if(userInfo.isAdmin()){
                            userInfo.getSession().getRemote().sendString(gson.toJson(message));
                            adminPresent = true;
                        }
                    }

                    if(!adminPresent && message.getType() != Message.Type.USER_LEFT){
                        //RETURN MESSAGE TO SENDER:
                        UserInfo ui = searchUserInfo(message.getUserInfo().getUsername());
                        Message msg = new Message();
                        UserInfo userInfo = new UserInfo();
                        userInfo.setId(-1);
                        userInfo.setUsername("SERVIDOR");
                        msg.setMessage("NO HAY ADMINISTRADOR DISPONIBLE");
                        msg.setUserInfo(userInfo);
                        msg.setType(Message.Type.USER_MESSAGE);
                        ui.getSession().getRemote().sendString(gson.toJson(msg));
                    }

                    break;
                case USER_MESSAGE:
                    //here we MUST have a to:
                    UserInfo target = searchUserInfo(message.getTo());
                    target.getSession().getRemote().sendString(gson.toJson(message));
                    break;
                case INIT:
                    //Send message back to self.
                    message.getUserInfo().getSession().getRemote().sendString(gson.toJson(message));
                    //If an admin has connected, tell all users:
                    if(message.getUserInfo().isAdmin()){
                        broadcastToAllNonAdmins("SE HA CONECTADO UN ADMINISTRADOR: " + message.getUserInfo().getUsername());
                    }
                    break;

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Function that returns a list of non admin users. Basically, it returns all connected users for admins
     * to see.
    @return list of non-admin users. This is for ADMIN use only.
     */
    public static List<UserInfo> getNonAdmins(){
        return userInfos.stream().filter(userInfo -> !userInfo.isAdmin()).collect(Collectors.toList());
    }

    private static UserInfo searchUserInfo(String user){
        for(UserInfo ui : ChatHandler.userInfos){
            if(ui.getUsername().equals(user)){
                return ui;
            }
        }

        return null; //user not found.
    }

    public static void broadcastToAllNonAdmins(String message){
        Message msg = new Message();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(-1);
        userInfo.setUsername("SERVIDOR");
        msg.setMessage(message);
        msg.setUserInfo(userInfo);
        msg.setType(Message.Type.USER_MESSAGE);
        List<UserInfo> nonAdmins = getNonAdmins();
        try{
            for(UserInfo ui : nonAdmins){
                ui.getSession().getRemote().sendString(gson.toJson(msg));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
