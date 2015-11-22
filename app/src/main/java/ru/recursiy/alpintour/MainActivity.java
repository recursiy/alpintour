package ru.recursiy.alpintour;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import ru.recursiy.alpintour.storage.Storage;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;

    Storage storage;
    SimpleCursorAdapter adapter;
    ListView routes;
    GeoMapAdapter mapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        storage = new Storage(this);

        routes = (ListView) findViewById(R.id.routes);

        //todo: description -> rock_name
        String[] from = new String[] { Storage.COLUMN_NAME, Storage.COLUMN_ROCK_NAME, Storage.COLUMN_DIFFICULT };
        int[] to = new int[] { R.id.name, R.id.rock_name, R.id.difficult };

        // создааем адаптер и настраиваем список
        adapter = new SimpleCursorAdapter(this, R.layout.route_list_element, null, from, to, 0);
        routes.setAdapter(adapter);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(MyCursorLoader.ALL_ROUTES_INFO, null, this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        mapAdapter = new GeoMapAdapter(this, mMap, null);
        getSupportLoaderManager().initLoader(MyCursorLoader.ALL_ROUTES_GEO, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, id, storage);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch(loader.getId())
        {
            case MyCursorLoader.ALL_ROUTES_INFO:
                adapter.swapCursor(cursor);
                break;
            case MyCursorLoader.ALL_ROUTES_GEO:
                mapAdapter.swapCursor(cursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {

        final static int ALL_ROUTES_INFO = 0x01;
        final static int ALL_ROUTES_GEO = 0x02;

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
                    //todo: rewrite request
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId() == R.id.sqlitedebug)
        {
            startActivity(new Intent(this, AndroidDatabaseManager.class));
            return true;
        }

        return false;
    }
}
