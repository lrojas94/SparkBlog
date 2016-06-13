package Classes.DataTables;

import com.google.gson.annotations.Expose;

import java.util.List;


/**
 * Created by luis on 6/12/16.
 */
public class SentParameters {
    @Expose
    private int draw;
    @Expose
    private int start;
    @Expose
    private int length;
    @Expose
    private SentSearch search;
    @Expose
    private SentColumn[] columns;
    @Expose
    private String tag;


    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public SentSearch getSearch() {
        return search;
    }

    public void setSearch(SentSearch search) {
        this.search = search;
    }

    public SentColumn[] getColumns() {
        return columns;
    }

    public void setColumns(SentColumn[] column) {
        this.columns = column;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
