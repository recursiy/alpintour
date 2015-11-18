package ru.recursiy.alpintour.storage;


import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ru.recursiy.alpintour.BuildConfig;

public class XmlContentProvider implements ContentProvider
{
    private XmlPullParser parser;
    final List<String> parsingTypes;
    Map<String, String> currentElement;



    enum Status {
        STARTED,
        CONSUMING_TAG,
        ENDED
    }

    Status status;

    public class XmlContentIterator implements Iterator<Map<String, String>>
    {

        @Override
        public boolean hasNext() {
            return currentElement != null || nextElement();
        }

        @Override
        public Map<String, String> next() {
            if (currentElement != null || nextElement())
            {
                Map<String, String> element = currentElement;
                currentElement = null;
                return element;
            }

            throw new NoSuchElementException();
        }

        @Override
        public void remove() {

        }
    }

    /**
     * Construct next element and put in into currentElement field
     * @return
     *  true if new element constructed
     *  false otherwise
     */
    boolean nextElement()
    {
        if(status == Status.ENDED)
            return false;

        HashMap<String, String> element = new HashMap<>(16);
        String tagName = "";
        String typeName = "";
        try {
            while(true) {
                parser.next();
                int eventType = parser.getEventType();
                if (status == Status.STARTED) {
                    if (eventType == XmlPullParser.END_DOCUMENT) {
                        //finished
                        status = Status.ENDED;
                        return false;
                    }
                    if (eventType == XmlPullParser.START_TAG && parsingTypes.contains(parser.getName())) {
                        //we found our tag
                        typeName = parser.getName();
                        status = Status.CONSUMING_TAG;
                        element.put(TYPE_TAG, typeName);
                    }
                }
                if (status == Status.CONSUMING_TAG) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (BuildConfig.DEBUG && tagName.equals(""))
                        {
                            throw new AssertionError();
                        }
                        element.put(tagName, parser.getText());
                        tagName = "";
                    }
                    else if (eventType == XmlPullParser.END_TAG && parser.getName().equals(typeName))
                    {
                        //element construction completed
                        currentElement = element;
                        status = Status.STARTED;
                        return true;
                    }
                    //todo: inserted tags
                }
            }
        }
        catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public XmlContentProvider(Context context, int xmlResource, String[] parsingTypes)
    {
        status = Status.STARTED;
        this.parsingTypes = Arrays.asList(parsingTypes);
        parser = context.getResources().getXml(xmlResource);
        try {
            if (parser.getEventType() == XmlPullParser.END_DOCUMENT) {
                //finished
                status = Status.ENDED;
            }
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Iterator<Map<String, String>> getIterator()
    {
        return new XmlContentIterator();
    }


}
