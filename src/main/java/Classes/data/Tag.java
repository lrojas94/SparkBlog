package Classes.data;

/**
 * Created by MEUrena on 5/31/16.
 * All rights reserved.
 */
public class Tag {

    private int id;
    private String description;

    public Tag() {}

    public Tag(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
