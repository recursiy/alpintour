package ru.recursiy.alpintour.storage;

import android.database.Cursor;

/**
 * Consumer to obtain cursor when load is finished
 */
public interface CursorConsumer {
    void consume(Cursor cursor);
}
