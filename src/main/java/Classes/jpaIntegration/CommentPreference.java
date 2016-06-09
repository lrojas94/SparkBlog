package Classes.jpaIntegration;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@Entity
@Table(name = "comment_preference")
public class CommentPreference implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "preference",nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Preference preference;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user",nullable = false)
    private User author;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comment",nullable = false)
    private Comment comment;

    public CommentPreference() {}

    public CommentPreference(Preference preference, User author, Comment comment) {
        this.setPreference(preference);
        this.author = author;
        this.setComment(comment);
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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
