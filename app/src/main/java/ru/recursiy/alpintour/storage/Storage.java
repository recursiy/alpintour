package ru.recursiy.alpintour.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.recursiy.alpintour.R;

/**
 * Class that store all the data
 */
public class Storage {
    DBHelper helper;
    SQLiteDatabase db;

    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_DESCRIPTION = "description";
    public final static String COLUMN_DIFFICULT = "difficult";

    public Storage(Context context)
    {
        //todo: it is not right place to elements list
        helper = new DBHelper(context, new XmlContentProvider(context, R.xml.test_data, new String[]{"region", "rock", "route"}));
        db = helper.getWritableDatabase();
    }

    public Cursor getAllRoutes()
    {
        return db.query("route", null, null, null, null, null, null);
    }
}
