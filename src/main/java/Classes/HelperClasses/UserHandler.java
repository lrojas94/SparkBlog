package Classes.HelperClasses;

import Classes.data.User;

/**
 * Created by MEUrena on 6/7/16.
 * All rights reserved.
 */
public class UserHandler extends DatabaseHandler<User> {

    private static UserHandler instance;

    private UserHandler() { super(User.class); }

    public static UserHandler getInstance() {
        if (instance == null) {
            instance = new UserHandler();
        }
        return instance;
    }
}
