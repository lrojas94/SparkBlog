package Classes.jpaIntegration;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@Entity
@Table(name = "article_preference")
public class ArticlePreference implements Serializable {

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
    @JoinColumn(name = "article",nullable = false)
    private Article article;

    public ArticlePreference() {}

    public ArticlePreference(Preference preference, User author, Article article) {
        this.setPreference(preference);
        this.author = author;
        this.article = article;
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

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }


}
