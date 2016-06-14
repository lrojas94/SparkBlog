package Classes.HelperClasses;

import Classes.jpaIntegration.User;
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

    public static List<Session>  connectedUsers = new ArrayList<>();

    public static void broadcastMessage(String sender, String message) throws Exception {
        try {
            for (Session user : connectedUsers) {
                user.getRemote().sendString(message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
