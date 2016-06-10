package Classes.jpaIntegration;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by lrojas on 5/31/16.
 * All rights reserved.
**/

public enum Preference{
    @SerializedName("like")
    LIKE,
    @SerializedName("dislike")
    DISLIKE,
    @SerializedName("neutral")
    NEUTRAL
}
