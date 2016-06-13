package Classes.DataTables;

import com.google.gson.annotations.Expose;

/**
 * Created by luis on 6/12/16.
 */
public class ReturnData{
    @Expose
    private int draw;
    @Expose
    private long recordsTotal;
    @Expose
    private long recordsFiltered;
    @Expose
    private Object[] data;
    @Expose
    private String error;


    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
