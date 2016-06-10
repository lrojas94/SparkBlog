package Classes.jpaIntegration;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@Entity
@Table(name = "comment_preference")
@NamedQueries({
        @NamedQuery(
                name = "CP.getAPByUserComment",
                query = "SELECT cp FROM CommentPreference cp WHERE cp.user.id = :userId " +
                        "AND cp.comment.id = :commentId"
        )
})
public class CommentPreference implements Serializable {
    public static String QUERY_NAME_GET_BY_USER_COMMENT= "CP.getAPByUserComment";
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "preference",nullable = false)
    @Enumerated(EnumType.STRING)
    private Preference preference;
    @ManyToOne
    @JoinColumn(name = "user",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "comment",nullable = false)
    private Comment comment;

    public CommentPreference() {}

    public CommentPreference(Preference preference, User user, Comment comment) {
        this.setPreference(preference);
        this.user = user;
        this.setComment(comment);
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return user;
    }

    public void setAuthor(User author) {
        this.user = author;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }


    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
