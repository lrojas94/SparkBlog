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

/**
 * Created by MEUrena on 6/13/16.
 * All rights reserved.
 */

public class ChatHandler {

    public static List<Session> connectedUsers = new ArrayList<>();
    public static List<UserInfo> userInfo = new ArrayList<>();

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
            UserInfo target = searchUserInfo(message.getTo());
            target.getSession().getRemote().sendString(gson.toJson(message));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static UserInfo searchUserInfo(String user){
        for(UserInfo ui : ChatHandler.userInfo){
            if(ui.getUsername().equals(user)){
                return ui;
            }
        }

        return null; //user not found.
    }
}
