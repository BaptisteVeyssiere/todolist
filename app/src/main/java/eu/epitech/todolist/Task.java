package eu.epitech.todolist;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private long id;
    private String title;
    private String description;
    private String date;
    private String commentary;
    private String status;
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public Task(Parcel in) {
        id = in.readLong();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        commentary = in.readString();
        status = in.readString();
    }


    public Task(long id, String title, String description, String date, String commentary, String status) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.commentary = commentary;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(commentary);
        dest.writeString(status);
    }
}
