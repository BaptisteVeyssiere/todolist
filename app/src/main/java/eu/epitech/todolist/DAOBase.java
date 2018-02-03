package eu.epitech.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAOBase {
    protected final static int VERSION = 1;
    protected final static String NOM = "database.db";
    protected SQLiteDatabase database = null;
    protected DatabaseHandler handler = null;

    public DAOBase(Context pContext) {
        this.handler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        database = handler.getWritableDatabase();
        return database;
    }

    public void close() {
        database.close();
    }

    public SQLiteDatabase getDb() {
        return database;
    }
}
