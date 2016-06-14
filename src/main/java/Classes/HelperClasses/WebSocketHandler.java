package Classes.HelperClasses;

import Classes.Main;
import Classes.jpaIntegration.User;
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
@WebSocket
public class WebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        System.out.println("Connecting user: " + session.getLocalAddress().getAddress().toString());
        ChatHandler.connectedUsers.add(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) throws Exception {
        System.out.println("Chat Closed with status (" + statusCode + "): " + reason);
        ChatHandler.connectedUsers.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("Receiving Message: " + message);
        String sender = session.getLocalAddress().getAddress().toString();
        ChatHandler.broadcastMessage(sender, message);
        try {
            session.getRemote().sendString("Message Received from: " + session.getLocalAddress().getAddress().toString());
        } catch (IOException ex) {
            throw ex;
        }
    }
}
