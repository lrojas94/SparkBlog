package Classes.HelperClasses;

import Classes.data.Comment;

/**
 * Created by MEUrena on 6/7/16.
 * All rights reserved.
 */
public class CommentHandler extends DatabaseHandler<Comment> {

    private static CommentHandler instance;

    private CommentHandler() { super(Comment.class); }

    public static CommentHandler getInstance() {
        if (instance == null) {
            instance = new CommentHandler();
        }
        return instance;
    }
}
