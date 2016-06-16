package Classes.WebSocket;

import com.google.gson.annotations.Expose;
import org.eclipse.jetty.websocket.api.Session;

/**
 * Created by luis on 6/15/16.
 */
public class UserInfo {
    @Expose
    private long id;
    @Expose
    private String username;
    @Expose
    private boolean admin = false;
    private Session session;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
