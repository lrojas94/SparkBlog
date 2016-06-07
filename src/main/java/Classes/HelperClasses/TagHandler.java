package Classes.HelperClasses;

import Classes.data.Tag;

/**
 * Created by MEUrena on 6/7/16.
 * All rights reserved.
 */
public class TagHandler extends DatabaseHandler<Tag> {

    private static TagHandler instance;

    private TagHandler() { super(Tag.class); }

    public static TagHandler getInstance() {
        if (instance == null) {
            instance = new TagHandler();
        }
        return instance;
    }
}
