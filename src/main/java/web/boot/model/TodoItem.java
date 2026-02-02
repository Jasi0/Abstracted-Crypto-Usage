package web.boot.model;

public class TodoItem {
    private long id;
    private String text;
    private boolean done;

    public TodoItem() {
    }

    public TodoItem(long id, String text, boolean done) {
        this.id = id;
        this.text = text;
        this.done = done;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}