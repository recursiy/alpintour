package ru.recursiy.alpintour;

import android.app.Fragment;
import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import ru.recursiy.alpintour.storage.CursorConsumer;
import ru.recursiy.alpintour.storage.CursorLoaderFactory;
import ru.recursiy.alpintour.storage.Storage;
import ru.recursiy.alpintour.storage.StorageCursorLoader;

/**
 * Routes list
 * */
public class RouteListFragment extends Fragment implements CursorConsumer {
    RouteListAdapter adapter;
    ExpandableListView routes;
    CursorLoaderFactory loaderFactory;
    LoaderManager loaderManager;
    Storage storage;

    public void init(CursorLoaderFactory loaderFactory, LoaderManager loaderManager, Storage storage)
    {
        assert(loaderFactory != null);
        assert(loaderManager != null);
        assert(storage != null);

        Log.d(Const.LOG_TAG, "init " + this);

        this.loaderFactory = loaderFactory;
        this.loaderManager = loaderManager;
        this.storage = storage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert(adapter != null);

        Log.d(Const.LOG_TAG, "onCreateView");

        Log.d(Const.LOG_TAG, "RouteListFramgent::onCreateView");
        View view = inflater.inflate(R.layout.route_list_fragment, container, false);

        routes = (ExpandableListView) view.findViewById(R.id.routes);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        assert(loaderFactory != null);
        assert(loaderManager != null);
        assert(storage != null);

        Log.d(Const.LOG_TAG, "onActivityCreated " + this);

        super.onActivityCreated(savedInstanceState);

        String[] from = new String[] { Storage.COLUMN_NAME, Storage.COLUMN_ROCK_NAME, Storage.COLUMN_DIFFICULT };
        int[] to = new int[] { R.id.name, R.id.rock_name, R.id.difficult };

        String[] childFrom = { Storage.COLUMN_DESCRIPTION };
        int[] childTo = { R.id.description };

        adapter = new RouteListAdapter(storage, getActivity(), null, R.layout.route_list_element, R.layout.route_list_element, from, to,
                R.layout.route_brief, R.layout.route_brief, childFrom, childTo);
        routes.setAdapter(adapter);
        // создаем лоадер для чтения данных
        loaderManager.initLoader(StorageCursorLoader.ALL_ROUTES_INFO, null, loaderFactory.createLoader(this));
    }

    @Override
    public void consume(Cursor cursor) {
        adapter.setGroupCursor(cursor);
    }
}
