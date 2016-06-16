package Classes.WebSocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

/**
 * Created by MEUrena on 6/13/16.
 * All rights reserved.
 */
@WebSocket(maxIdleTime = 600000)
public class WebSocketHandler {

    private static long ids = 0;
    private static GsonBuilder gsonBuilder = new GsonBuilder();
    private static Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        System.out.println("Connecting user: " + session.getLocalAddress().getAddress().toString());
        ChatHandler.connectedUsers.add(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) throws Exception {
        System.out.println("Chat Closed with status code (" + statusCode + "): " + reason);
        ChatHandler.connectedUsers.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try{
            Message msg = gson.fromJson(message,Message.class);
            switch (msg.getType()){
                case INIT:
                    //Create new user :)!
                    UserInfo userInfo = new UserInfo();
                    userInfo.setSession(session);
                    userInfo.setId(ids++);
                    userInfo.setUsername(msg.getUserInfo().getUsername());
                    ChatHandler.userInfo.add(userInfo);
                    msg.setUserInfo(userInfo);
                    msg.setTo(userInfo.getUsername());
                    //Return it:
                    ChatHandler.sendMessage(msg);
                    break;
                case MESSAGE:
                    /*
                    THIS IS FOR TESTING PURPOSES ONLY.
                    IDEALLY, IF USER IS NOT ADMIN, THEN THIS SHOULD BE REDIRECTED TO ALL
                    ADMINS. ELSE, MESSAGE DATA SHOULD ALREADY CONTAIN A TARGET USER (TO).
                     */
                    msg.setMessage("REDIRECT FROM SERVER: " + msg.getMessage());
                    msg.setTo(msg.getUserInfo().getUsername()); //REDIRECT MESSAGE
                    ChatHandler.sendMessage(msg);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
