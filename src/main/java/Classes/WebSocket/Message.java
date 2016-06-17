package Classes.WebSocket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by luis on 6/15/16.
 */
public class Message {
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Only used when type = INIT && userInfo.isAdmin() == true
     */
    public List<UserInfo> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(List<UserInfo> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public enum Type{
        @SerializedName("INIT")
        INIT,
        @SerializedName("USER_MESSAGE")
        USER_MESSAGE,
        @SerializedName("ADMIN_MESSAGE")
        ADMIN_MESSAGE,
        @SerializedName("USER_JOINED")
        USER_JOINED,
        @SerializedName("USER_LEFT")
        USER_LEFT
    }

    @Expose
    private Type type;
    @Expose
    private UserInfo userInfo; //-> Note, this must be initialized actually.
    @Expose
    private String message;
    /**
     * Only used when type = USER_MESSAGE
     */
    @Expose
    private String to; //username of target. ADMIN otherwise.
    @Expose
    private List<UserInfo> onlineUsers;




}
