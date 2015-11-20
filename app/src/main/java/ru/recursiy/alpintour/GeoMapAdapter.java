package ru.recursiy.alpintour;

import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.recursiy.alpintour.storage.Storage;

/**
 * Provides marks on map
 */
public class GeoMapAdapter {
    Context context;
    GoogleMap map;
    Cursor cursor;

    public GeoMapAdapter(Context ctx, GoogleMap map, Cursor cursor)
    {
        context = ctx;
        this.map = map;
        this.cursor = cursor;
        applyCursor();
    }

    private void applyCursor()
    {
        if (cursor != null)
        {
            if (cursor.moveToFirst()) {

                // определяем номера столбцов по имени в выборке
                int xIndex = cursor.getColumnIndex(Storage.COLUMN_GEO_X);
                int yIndex = cursor.getColumnIndex(Storage.COLUMN_GEO_Y);
                int nameIndex = cursor.getColumnIndex(Storage.COLUMN_NAME);

                do {
                    double x = cursor.getDouble(xIndex);
                    double y = cursor.getDouble(yIndex);
                    String name = cursor.getString(nameIndex);
                    LatLng geo = new LatLng(x, y);
                    map.addMarker(new MarkerOptions().position(geo).title(name));
                } while (cursor.moveToNext());
            }
        }
    }

    //todo: swapCursor, like in SimpleCursorAdapter
    private void setCursor(Cursor newCursor)
    {
        cursor = newCursor;
        applyCursor();
    }
}
