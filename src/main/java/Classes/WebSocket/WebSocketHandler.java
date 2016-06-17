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
        ChatHandler.userInfos.stream()
                .filter(userInfo -> userInfo.getSession().equals(session))
                .findFirst()
                .map(userInfo -> {
                    if(!userInfo.isAdmin()){
                        //should broadcast to admin:
                        Message msg = new Message();
                        msg.setType(Message.Type.USER_LEFT);
                        msg.setUserInfo(userInfo);
                        msg.setMessage("");
                        ChatHandler.sendMessage(msg);
                    }
                    else{
                        ChatHandler.broadcastToAllNonAdmins("ADMIN - " + userInfo.getUsername() + " - HA DEJADO EL CHAT");
                    }
                    ChatHandler.userInfos.remove(userInfo);
                    return userInfo;
                });
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
                    userInfo.setAdmin(msg.getUserInfo().isAdmin());
                    msg.setUserInfo(userInfo);
                    msg.setTo(userInfo.getUsername());
                    ChatHandler.userInfos.add(userInfo);

                    if(userInfo.isAdmin()){
                        msg.setOnlineUsers(ChatHandler.getNonAdmins());
                    }
                    else
                    {
                        // Tell admins:
                        msg.setType(Message.Type.USER_JOINED);
                        ChatHandler.sendMessage(msg);
                        //reset to default
                        msg.setType(Message.Type.INIT);
                    }

                    //Return it:
                    ChatHandler.sendMessage(msg);
                    break;
                case ADMIN_MESSAGE:
                case USER_MESSAGE:
                    ChatHandler.sendMessage(msg);
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
