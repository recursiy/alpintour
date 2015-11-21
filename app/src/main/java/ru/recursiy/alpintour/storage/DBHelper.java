package ru.recursiy.alpintour.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import ru.recursiy.alpintour.Const;

/**
 * This class is needed for create and update database
 */
public class DBHelper extends SQLiteOpenHelper {
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
    final String GEO_X_TAG = "geo_x";
    final String GEO_Y_TAG = "geo_y";

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
                + Storage.COLUMN_GEO_X + " real,"
                + Storage.COLUMN_GEO_Y + " real,"
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

    static String makeForeignKey(String tableName)
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
        String query = "insert into " + TABLE_ROCK + " ("
                + Storage.COLUMN_NAME + ", "
                + Storage.COLUMN_DESCRIPTION + ", "
                + makeForeignKey(TABLE_REGION) + ") "
                + " VALUES ('" + element.get(NAME_TAG) + "', "
                + "'" + element.get(DESCRIPTION_TAG) + "', "
                + "(SELECT " + Storage.COLUMN_ID + " FROM " + TABLE_REGION
                + " WHERE " + TABLE_REGION + "." + Storage.COLUMN_NAME + "='" + element.get(REGION_TAG) + "')"
                + ")";
        db.execSQL(query);
        /*ContentValues cv = new ContentValues();
        cv.put(Storage.COLUMN_NAME, element.get(NAME_TAG));
        cv.put(Storage.COLUMN_DESCRIPTION, element.get(DESCRIPTION_TAG));
        cv.put(makeForeignKey(TABLE_REGION),
                "(SELECT " + Storage.COLUMN_ID + " FROM " + TABLE_REGION
                        + " WHERE " + TABLE_REGION + "." + Storage.COLUMN_NAME + "=" + element.get(REGION_TAG) + ")");
        db.insert(TABLE_ROCK, null, cv);*/
    }

    void insertRoute(Map<String, String> element, SQLiteDatabase db)
    {
        String query = "insert into " + TABLE_ROUTE + " ("
                + Storage.COLUMN_NAME + ", "
                + Storage.COLUMN_DESCRIPTION + ", "
                + Storage.COLUMN_DIFFICULT + ", "
                + Storage.COLUMN_GEO_X + ", "
                + Storage.COLUMN_GEO_Y + ", "
                + makeForeignKey(TABLE_ROCK) + ") "
                + " VALUES ('" + element.get(NAME_TAG) + "', "
                + "'" + element.get(DESCRIPTION_TAG) + "', "
                + "'" + element.get(DIFFICULT_TAG) + "', "
                + "'" + element.get(GEO_X_TAG) + "', "
                + "'" + element.get(GEO_Y_TAG) + "', "
                + "(SELECT " + Storage.COLUMN_ID + " FROM " + TABLE_ROCK
                + " WHERE " + TABLE_ROCK + "." + Storage.COLUMN_NAME + "='" + element.get(ROCK_TAG) + "')"
                + ")";
        db.execSQL(query);
        /*ContentValues cv = new ContentValues();
        cv.put(Storage.COLUMN_NAME, element.get(NAME_TAG));
        cv.put(Storage.COLUMN_DESCRIPTION, element.get(DESCRIPTION_TAG));
        cv.put(Storage.COLUMN_DIFFICULT, element.get(DIFFICULT_TAG));
        cv.put(Storage.COLUMN_GEO_X, element.get(GEO_X_TAG));
        cv.put(Storage.COLUMN_GEO_Y, element.get(GEO_Y_TAG));
        cv.put(makeForeignKey(TABLE_ROCK),
                "(SELECT " + Storage.COLUMN_ID + " FROM " + TABLE_ROCK
                        + " WHERE " + ROCK_TAG + "." + Storage.COLUMN_NAME + "=" + element.get(ROCK_TAG) + ")");
        db.insert(TABLE_ROUTE, null, cv);*/
    }

    //sqlite debug
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}
