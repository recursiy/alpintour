package ru.recursiy.alpintour;

import android.content.Context;
import android.database.Cursor;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ru.recursiy.alpintour.storage.CursorConsumer;
import ru.recursiy.alpintour.storage.Storage;

/**
 * Provides marks on map
 */
public class GeoMapAdapter implements CursorConsumer {
    Context context;
    GoogleMap map;
    Cursor cursor;
    ArrayList<Marker> markers;

    public GeoMapAdapter(Context ctx, GoogleMap map, Cursor cursor)
    {
        context = ctx;
        this.map = map;
        this.cursor = cursor;
        applyCursor();
    }

    private void applyCursor()
    {
        if (markers != null)
        {
            for(Marker marker : markers)
            {
                marker.remove();
            }
            markers = null;
        }
        if (cursor != null)
        {
            markers = new ArrayList<>(cursor.getCount());
            if (cursor.moveToFirst())
            {
                int xIndex = cursor.getColumnIndex(Storage.COLUMN_GEO_X);
                int yIndex = cursor.getColumnIndex(Storage.COLUMN_GEO_Y);
                int nameIndex = cursor.getColumnIndex(Storage.COLUMN_NAME);

                do {
                    double x = cursor.getDouble(xIndex);
                    double y = cursor.getDouble(yIndex);
                    String name = cursor.getString(nameIndex);
                    LatLng geo = new LatLng(x, y);
                    markers.add(map.addMarker(new MarkerOptions().position(geo).title(name)));
                } while (cursor.moveToNext());
            }
        }
    }

    public Cursor swapCursor(Cursor newCursor)
    {
        Cursor oldCursor = cursor;
        cursor = newCursor;
        applyCursor();
        return oldCursor;
    }

    @Override
    public void consume(Cursor cursor) {
        swapCursor(cursor);
    }
}
