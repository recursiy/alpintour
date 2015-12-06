package ru.recursiy.alpintour.storage;

import android.app.LoaderManager;
import android.content.Context;
import android.database.Cursor;

/**
 * I don't really like the way how it's realizing, but I don't know, how NOT to use factory
 */
public class CursorLoaderFactory {
    Storage storage;
    Context context;

    public CursorLoaderFactory(Context context, Storage storage)
    {
        assert(context != null);
        assert(storage != null);
        this.context = context;
        this.storage = storage;
    }

    public LoaderManager.LoaderCallbacks<Cursor> createLoader(CursorConsumer consumer)
    {
        return new StorageCursorLoader(context, storage, consumer);
    }
}
