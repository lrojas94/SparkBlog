package Classes.jpaIntegration;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
@Entity
@Table(name = "tags")
@NamedQueries({
        @NamedQuery(
                name = "getTagByDescription",
                query = "SELECT t FROM Tag t WHERE t.description = :description"
        )
})
public class Tag implements Serializable {

    public static String QUERY_NAME_TAG_BY_DESC = "getTagByDescription";

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "description",nullable = false,unique = true)
    private String description;
    @ManyToMany(mappedBy = "tags",cascade = CascadeType.ALL)
    private Set<Article> articles = new HashSet<>();

    public Tag() {}

    public Tag(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tag){
            return ((Tag) obj).getDescription().equals(description);
        }
        return super.equals(obj);
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
}
