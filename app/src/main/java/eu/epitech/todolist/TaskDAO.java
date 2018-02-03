package eu.epitech.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TaskDAO extends DAOBase {
    public static final String KEY = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String COMMENTARY = "commentary";
    public static final String STATUS = "status";

    public static final String TABLE_NAME = "Task";

    public TaskDAO(Context pContext) {
        super(pContext);
    }

    public void add(Task elem) {
        ContentValues value = new ContentValues();
        value.put(TITLE, elem.getTitle());
        value.put(DESCRIPTION, elem.getDescription());
        value.put(DATE, elem.getDate());
        value.put(COMMENTARY, elem.getCommentary());
        value.put(STATUS, elem.getStatus());
        database.insert(TABLE_NAME, null, value);
    }

    public void delete(long id) {
        database.delete(TABLE_NAME, KEY + " = ?", new String[] {String.valueOf(id)});
    }

    public void edit(Task elem) {
        ContentValues value = new ContentValues();
        value.put(TITLE, elem.getTitle());
        value.put(DESCRIPTION, elem.getDescription());
        value.put(DATE, elem.getDate());
        value.put(COMMENTARY, elem.getCommentary());
        value.put(STATUS, elem.getStatus());
        database.update(TABLE_NAME, value, KEY + " = ?", new String[] {String.valueOf(elem.getId())});
    }

    public Cursor select(String request, String[] args) {
        return (database.rawQuery(request, args));
    }
}
