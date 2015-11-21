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

    //primitive column names
    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_DESCRIPTION = "description";
    public final static String COLUMN_DIFFICULT = "difficult";
    public final static String COLUMN_GEO_X = "geo_x";
    public final static String COLUMN_GEO_Y = "geo_y";

    //complex names
    public final static String COLUMN_ROCK_NAME = "rock_name";

    //element types
    public final static String ELEMENT_REGION = "region";
    public final static String ELEMENT_ROCK = "rock";
    public final static String ELEMENT_ROUTE = "route";

    public final static String[] availableElements = {ELEMENT_REGION, ELEMENT_ROCK, ELEMENT_ROUTE};

    public Storage(Context context)
    {
        //todo: it is not right place to elements list
        helper = new DBHelper(context, new XmlContentProvider(context, R.xml.test_data, availableElements));
        db = helper.getWritableDatabase();
    }

    public Cursor getRoutesInfo(/*filter*/)
    {
        String table = DBHelper.TABLE_ROUTE + " as route"
                + " inner join " + DBHelper.TABLE_ROCK + " as rock "
                + " on route." + DBHelper.makeForeignKey(DBHelper.TABLE_ROCK) + " = rock." + COLUMN_ID;
        String columns[] = {
                "route." + COLUMN_ID + " as " + COLUMN_ID,
                "route." + COLUMN_NAME + " as " + COLUMN_NAME,
                "route." + COLUMN_DESCRIPTION + " as " + COLUMN_DESCRIPTION,
                "route." + COLUMN_DIFFICULT + " as " + COLUMN_DIFFICULT,
                "rock." + COLUMN_NAME + " as " + COLUMN_ROCK_NAME};

        return db.query(table, columns, null, null, null, null, null);
    }

    public Cursor getAllRoutes()
    {
        return db.query("route", null, null, null, null, null, null);
    }
}
