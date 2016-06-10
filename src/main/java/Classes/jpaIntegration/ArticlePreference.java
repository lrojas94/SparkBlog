package Classes.jpaIntegration;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@Entity
@Table(name = "article_preference")
@NamedQueries({
        @NamedQuery(
                name = "AP.getAPByUserArticle",
                query = "SELECT ap FROM ArticlePreference ap WHERE ap.user.id = :userId " +
                        "AND ap.article.id = :articleId"
        )
})
public class ArticlePreference implements Serializable {

    public static String QUERY_NAME_GET_BY_USER_ARTICLE = "AP.getAPByUserArticle";

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
    @JoinColumn(name = "article",nullable = false)
    private Article article;

    public ArticlePreference() {}

    public ArticlePreference(Preference preference, User author, Article article) {
        this.setPreference(preference);
        this.user = author;
        this.article = article;
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
