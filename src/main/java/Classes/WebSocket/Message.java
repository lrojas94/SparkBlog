package Classes.WebSocket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public enum Type{
        @SerializedName("INIT")
        INIT,
        @SerializedName("MESSAGE")
        MESSAGE
    }

    @Expose
    private Type type;
    @Expose
    private UserInfo userInfo; //-> Note, this must be initialized actually.
    @Expose
    private String message;
    @Expose
    private String to; //username of target. ADMIN otherwise.




}
