package ru.geekbrains.android.mynotes;

import java.io.Serializable;
import java.util.Date;

public class MyNote implements Serializable {

    private String title;
    private String describe;
    private final Date create_at;

    public MyNote(String title, String describe) {
        this.title = title;
        this.describe = describe;
        create_at = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Date getCreate_at() {
        return create_at;
    }
}
