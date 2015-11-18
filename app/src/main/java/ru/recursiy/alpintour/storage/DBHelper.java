package ru.recursiy.alpintour.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;

import ru.recursiy.alpintour.Const;

/**
 * This class is needed for create and update database
 */
class DBHelper extends SQLiteOpenHelper {
    final static int version = 1;

    final static String DB_NAME = "alpintour";

    final static String TABLE_REGION = "region";
    final static String TABLE_ROCK = "rock";
    final static String TABLE_ROUTE = "route";

    //common
    final String NAME_TAG = "name";
    final String DESCRIPTION_TAG = "description";

    //region
    final String REGION_TAG = "region";

    //rock
    final String ROCK_TAG = "rock";

    //route
    final String ROUTE_TAG = "route";
    final String DIFFICULT_TAG = "difficult";

    ContentProvider provider;



    public DBHelper(Context context, ContentProvider provider) {
        super(context, DB_NAME, null, version);
        this.provider = provider;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(Const.LOG_TAG, "--- onCreate database ---");

        db.execSQL("create table " + TABLE_REGION + "("
                + Storage.COLUMN_ID + " integer primary key autoincrement,"
                + Storage.COLUMN_NAME + " text,"
                + Storage.COLUMN_DESCRIPTION + " text" + ");");

        db.execSQL("create table " + TABLE_ROCK + " ("
                + Storage.COLUMN_ID + " integer primary key autoincrement,"
                + Storage.COLUMN_NAME + " text,"
                + Storage.COLUMN_DESCRIPTION + " text,"
                + makeForeignKey(TABLE_REGION) + " integer"
                + ");");


        db.execSQL("create table " + TABLE_ROUTE + "("
                + Storage.COLUMN_ID + " integer primary key autoincrement,"
                + Storage.COLUMN_NAME + " text,"
                + Storage.COLUMN_DESCRIPTION + " text,"
                + Storage.COLUMN_DIFFICULT + " text,"
                + makeForeignKey(TABLE_ROCK) + " integer"
                + ");");


        for(Iterator<Map<String, String>> iter = provider.getIterator(); iter.hasNext();)
        {
            Map<String, String> element = iter.next();
            Log.d(Const.LOG_TAG, "__element__");
            for(Map.Entry<String, String> entry : element.entrySet())
            {
                Log.d(Const.LOG_TAG, entry.getKey() + " : " + entry.getValue());
            }

            switch(element.get(ContentProvider.TYPE_TAG))
            {
                case REGION_TAG:
                    insertRegion(element, db);
                    break;
                case ROCK_TAG:
                    insertRock(element, db);
                    break;
                case ROUTE_TAG:
                    insertRoute(element, db);
                    break;
                default:
                    Log.e(Const.LOG_TAG, "Unknown type of element");
                    throw new AssertionError("Unknown type of element");
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(Const.LOG_TAG, "On upgrade db called, smth goes wrong");
    }

    String makeForeignKey(String tableName)
    {
        return tableName + "_id";
    }

    void insertRegion(Map<String, String> element, SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        cv.put(Storage.COLUMN_NAME, element.get(NAME_TAG));
        cv.put(Storage.COLUMN_DESCRIPTION, element.get(DESCRIPTION_TAG));
        db.insert(TABLE_REGION, null, cv);
    }

    void insertRock(Map<String, String> element, SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        cv.put(Storage.COLUMN_NAME, element.get(NAME_TAG));
        cv.put(Storage.COLUMN_DESCRIPTION, element.get(DESCRIPTION_TAG));
        db.insert(TABLE_ROCK, null, cv);
    }

    void insertRoute(Map<String, String> element, SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        cv.put(Storage.COLUMN_NAME, element.get(NAME_TAG));
        cv.put(Storage.COLUMN_DESCRIPTION, element.get(DESCRIPTION_TAG));
        cv.put(Storage.COLUMN_DIFFICULT, element.get(DIFFICULT_TAG));
        db.insert(TABLE_ROUTE, null, cv);
    }
}
