package Classes.DataTables;

import com.google.gson.annotations.Expose;

/**
 * Created by luis on 6/12/16.
 */
public class SentColumn {
    @Expose
    private String data;
    @Expose
    private String name;
    @Expose
    private boolean searchable;
    @Expose
    private boolean orderable;
    @Expose
    private SentSearch search;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }

    public SentSearch getSearch() {
        return search;
    }

    public void setSearch(SentSearch search) {
        this.search = search;
    }
}
