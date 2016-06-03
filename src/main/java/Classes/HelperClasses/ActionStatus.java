package Classes.HelperClasses;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by luis on 6/3/16.
 */
public class ActionStatus {
    @Expose
    private String status;
    @Expose
    private ArrayList<String> errors;
    @Expose
    private Object returnObject;

    public ActionStatus(){
        errors = new ArrayList<String>();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }
}
