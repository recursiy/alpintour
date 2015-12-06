package ru.recursiy.alpintour.storage;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import ru.recursiy.alpintour.BuildConfig;
import ru.recursiy.alpintour.Const;

/**
 * Using for async load cursors
 */
public class StorageCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public final static int ALL_ROUTES_INFO = 0x01;
    public final static int ALL_ROUTES_GEO = 0x02;

    Storage storage;
    Context context;
    CursorConsumer consumer;

    public StorageCursorLoader(Context context, Storage storage, CursorConsumer consumer)
    {
        this.storage = storage;
        this.context = context;
        this.consumer = consumer;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(context, id, storage);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        consumer.consume(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends android.content.CursorLoader {
        Storage storage;
        final int id;

        public MyCursorLoader(Context context, int id, Storage storage) {
            super(context);
            this.storage = storage;
            this.id = id;
        }

        @Override
        public Cursor loadInBackground() {
            switch(id)
            {
                case ALL_ROUTES_INFO:
                    return storage.getRoutesInfo();
                case ALL_ROUTES_GEO:
                    return storage.getAllRoutes();
            }
            Log.e(Const.LOG_TAG, "Unknown log");
            if(BuildConfig.DEBUG)
                throw new AssertionError("Unknown log");
            return null;
        }

        public int getId()
        {
            return id;
        }
    }
}
