package ru.recursiy.alpintour;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorTreeAdapter;

import ru.recursiy.alpintour.storage.Storage;

/**
 * Adapter for routes list
 */
public class RouteListAdapter extends SimpleCursorTreeAdapter {
    Storage storage;

    public RouteListAdapter(Storage storage, Context context, Cursor cursor, int collapsedGroupLayout, int expandedGroupLayout, String[] groupFrom, int[] groupTo, int childLayout, int lastChildLayout, String[] childFrom, int[] childTo) {
        super(context, cursor, collapsedGroupLayout, expandedGroupLayout, groupFrom, groupTo, childLayout, lastChildLayout, childFrom, childTo);
        this.storage = storage;
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        //todo: async load through cursor loader
        int idIndex = groupCursor.getColumnIndex(Storage.COLUMN_ID);
        return storage.getBriefRouteDescription(groupCursor.getInt(idIndex));
    }
}
