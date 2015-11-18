package ru.recursiy.alpintour;


//todo: move from support.v4
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.recursiy.alpintour.storage.Storage;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;

    Storage storage;
    SimpleCursorAdapter adapter;
    ListView routes;

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
        String[] from = new String[] { Storage.COLUMN_NAME, Storage.COLUMN_DESCRIPTION, Storage.COLUMN_DIFFICULT };
        int[] to = new int[] { R.id.name, R.id.rock_name, R.id.difficult };

        // создааем адаптер и настраиваем список
        adapter = new SimpleCursorAdapter(this, R.layout.route_list_element, null, from, to, 0);
        routes.setAdapter(adapter);

        // создаем лоадер для чтения данных
        getSupportLoaderManager().initLoader(0, null, this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, storage);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {

        Storage storage;

        public MyCursorLoader(Context context, Storage storage) {
            super(context);
            this.storage = storage;
        }

        @Override
        public Cursor loadInBackground() {
            return storage.getAllRoutes();
        }

    }
}
