package ru.recursiy.alpintour;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import ru.recursiy.alpintour.storage.CursorConsumer;
import ru.recursiy.alpintour.storage.CursorLoaderFactory;
import ru.recursiy.alpintour.storage.Storage;
import ru.recursiy.alpintour.storage.StorageCursorLoader;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Storage storage;

    GeoMapAdapter mapAdapter;

    RouteListFragment routeList;

    CursorLoaderFactory loaderFactory;
    LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storage = new Storage(this);
        loaderManager = getLoaderManager();
        loaderFactory = new CursorLoaderFactory(this, storage);

        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        routeList = (RouteListFragment) getFragmentManager().findFragmentById(R.id.routes);
        routeList.init(loaderFactory, loaderManager, storage);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        mapAdapter = new GeoMapAdapter(this, mMap, null);
        getLoaderManager().initLoader(StorageCursorLoader.ALL_ROUTES_GEO, null, loaderFactory.createLoader(mapAdapter));
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
