package Classes.JsonClasses;

import Classes.jpaIntegration.Preference;
import com.google.gson.annotations.Expose;

/**
 * Created by luis on 6/9/16.
 */
public class UserPreference {
    @Expose
    private boolean isArticle;
    @Expose
    private int preferenceId;
    @Expose
    private int userId;
    @Expose
    private Preference preference;


    public boolean isArticle() {
        return isArticle;
    }

    public void setArticle(boolean article) {
        isArticle = article;
    }

    public int getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(int preferenceId) {
        this.preferenceId = preferenceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }
}
