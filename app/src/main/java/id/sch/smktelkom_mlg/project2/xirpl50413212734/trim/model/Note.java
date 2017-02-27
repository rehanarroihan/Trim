package id.sch.smktelkom_mlg.project2.xirpl50413212734.trim.model;

/**
 * Created by Rehan on 2/26/2017.
 */

public class Note {
    private String title;
    private String content;
    private String time;

    public Note() {
        //empty constructor
    }

    public Note(String title, String content, String time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
