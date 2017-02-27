package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.model;

/**
 * Created by Rehan on 2/27/2017.
 */

public class Todolist {
    String title;
    String detail;
    String date;

    public Todolist() {
        //empty contructor
    }

    public Todolist(String title, String detail, String date) {
        this.title = title;
        this.detail = detail;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
