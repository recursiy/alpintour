package ru.recursiy.alpintour.storage;

import java.util.Iterator;
import java.util.Map;

/**
 * Provides iterator to data
 * Every element is containing in map
 * Value "type" using to check what type is element, others are type-specified
 */
interface ContentProvider {
    String TYPE_TAG = "type";

    Iterator<Map<String, String>> getIterator();
}
