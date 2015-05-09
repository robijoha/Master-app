package se.ixdmaster.gabre.inobi_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Icon;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.InfoWindow;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    public static final long TIME = 30000;
    public static final int TEXT_SIZE = 16;
    public static final int NUMB_SIZE = 20;
    public static final int TITLE_SIZE = 30;

    protected static final int USER_ID = 0;
    protected static boolean FLAG = false;
    protected static boolean REDRAW = false;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private Location myLocation;
    private MapView mapView;
    private ArrayList<Marker> markers;
    private ArrayList<View.OnTouchListener> onTouchListeners;
    private ShareActionProvider mShareActionProvider;
    private Intent mShareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_main);

        //TODO: Om det är första gången som appen öppnas. Skapa ett id för avnvändaren. Kolla med MoE, om namn eller nummer
        //TODO: Ljud?
        locationHelper();
        setupMap();
        buttonHandler();
        loadData();
//
//        Spot spot = new Spot(0, "Martin Shop", 57.699504, 11.951287, new Date().toString(),0, 0 ,0 ,0 ,0 ,0);
//        manager.addSpot(spot);
//        addMarkerWithListener(spot);
//
//        spot = new Spot(1, "Jerntorgets brygghus", 57.699577, 11.951993, new Date().toString(),0, 0 ,0 ,0 ,0 ,0);
//        manager.addSpot(spot);
//        addMarkerWithListener(spot);
//
//        spot = new Spot(2, "Burger King", 57.699527, 11.952981, new Date().toString(), 0, 0 ,0 ,0 ,0 ,0);
//        manager.addSpot(spot);
//        addMarkerWithListener(spot);
//
//        spot = new Spot(3, "Burger King", 57.699527, 11.952981, new Date().toString(), 0, 0 ,0 ,0 ,0 ,0);
//        manager.addSpot(spot);
//        addMarkerWithListener(spot);
//
//        spot = new Spot(4, "Way Cup", 57.699888, 11.951889, new Date().toString(),0, 0 ,0 ,0 ,0 ,0);
//        manager.addSpot(spot);
//        addMarkerWithListener(spot);
//
//        spot = new Spot(5, "Way Cup", 57.699888, 11.951889, new Date().toString(),0, 0 ,0 ,0 ,0 ,0);
//        manager.addSpot(spot);
//        addMarkerWithListener(spot);
    }

    private void loadData(){
        markers = new ArrayList<>();
        onTouchListeners = new ArrayList<>();
        Manager manager = Manager.getInstance();
        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String savedData = settings.getString("DATA", "[]");
        try {
            JSONArray dataArray = new JSONArray(savedData);
            if (dataArray.length() > 0 && manager.count() == 0){
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject data = dataArray.getJSONObject(i);
                    Spot spot = new Spot(
                            data.getInt("spotId"),
                            data.getString("name"),
                            data.getDouble("lat"),
                            data.getDouble("lng"),
                            data.getString("date"),
                            data.getInt("userId"),
                            data.getInt("peoplePresent"),
                            data.getInt("nrOfConv"),
                            data.getInt("nrOfcollab"),
                            data.getInt("percentSitting"),
                            data.getInt("flowCounter"));
                    manager.addSpot(spot);
                    addMarkerWithListener();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        shareHelper();
    }

    private void setupMap (){
        mapView = (MapView) findViewById(R.id.mapview);
//        UserLocationOverlay myLocationOverlay = new UserLocationOverlay(new GpsLocationProvider(getApplicationContext()) , mapView);
//        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.locate);
//        myLocationOverlay.setPersonBitmap(icon); // locate bilden
//        myLocationOverlay.setOverlayCircleColor(0xFF00FF00);
//        myLocationOverlay.enableMyLocation();
//        myLocationOverlay.setDrawAccuracyEnabled(true);
//        mapView.getOverlays().add(myLocationOverlay);
        LatLng ostersund = new LatLng(63.178889, 14.636389);
        mapView.setCenter(ostersund);
        mapView.setZoom(6);
        mapView.setMaxZoomLevel(20);
        mapView.setMinZoomLevel(2);
        mapView.setUserLocationEnabled(true);
        mapView.setMapViewListener(new MapViewListener() {
            @Override
            public void onShowMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onHideMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onTapMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onLongPressMarker(MapView mapView, Marker marker) {

            }

            @Override
            public void onTapMap(MapView mapView, ILatLng iLatLng) {

            }

            @Override
            public void onLongPressMap(MapView mapView, ILatLng iLatLng) {
                Intent intent = new Intent(getApplicationContext(), CreateSpot.class);
                intent.putExtra("Lat", iLatLng.getLatitude());
                intent.putExtra("Lng", iLatLng.getLongitude());
                startActivity(intent);
            }
        });
    }

    public void buttonHandler (){
        Button locate = (Button) findViewById(R.id.button_find_user);
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapView.getUserLocation() != null) {
                    mapView.goToUserLocation(true);
                }

            }
        });

        //TODO: FORSKA I DET HÄR
//        List<Overlay> list = mapView.getOverlays();
//


        Button add = (Button) findViewById(R.id.button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myLocation != null) {
                    Intent intent = new Intent(getApplicationContext(), CreateSpot.class);
                    intent.putExtra("Lat", myLocation.getLatitude());
                    intent.putExtra("Lng", myLocation.getLongitude());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Wait for GPS position, please", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /*
    The method which handles location updates etc
     */
    public void locationHelper () {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Kontrollera att din GPS är påslagen")
                .setCancelable(false)
                .setPositiveButton("Gärna!", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Du är inte min pappa", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
//                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /*
    Keeps a small marker on the map to let the user know where they are.
     */
    private void updateLocation(Location location) {
        this.myLocation = location;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FLAG){
            FLAG = false;
            addMarkerWithListener();
        }else if(REDRAW){
            for(Marker m : markers){
                mapView.removeMarker(m);
            }
            onTouchListeners.clear();
            for(Spot s : Manager.getInstance().getList()){
                addMarkerWithListener(s);
            }
            REDRAW = false;
            mapView.closeCurrentTooltip();
            Manager.getInstance().saveChanges(getSharedPreferences("PREFS", Context.MODE_PRIVATE));
        }

        if(locationManager != null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

        shareHelper();

    }

    private void addMarkerWithListener() {
        final Manager manager = Manager.getInstance();
        final Spot spot = manager.getLastAddedSpot();
        Marker marker = new Marker(spot.getName(), spot.getDate(), new LatLng(spot.getLat(),spot.getLng()));
        InfoWindow infoWindow = marker.getToolTip(mapView);
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), SpotInfo.class);
                intent.putExtra("Position", spot.getSpotId());
                startActivity(intent);
                return true;
            }
        };
        infoWindow.setOnTouchListener(onTouchListener);
        marker.setToolTip(infoWindow);
        //TODO: Ändra här så att det blir rätt ikon för spots på kartan
//        marker.setIcon(new Icon(getResources().getDrawable(R.drawable.one)));
        marker.setIcon(new Icon(this, Icon.Size.MEDIUM, "circle", "4caf50"));
        mapView.addMarker(marker);
        markers.add(marker);
        onTouchListeners.add(onTouchListener);
        Manager.getInstance().saveChanges(getSharedPreferences("PREFS", Context.MODE_PRIVATE));
    }

    //Called in the beginning to repopulate.
    private void  addMarkerWithListener(Spot spot) {
        final Spot currentSpot = spot;
        Marker marker = new Marker(spot.getName(), spot.getDate(), new LatLng(spot.getLat(),spot.getLng()));
        InfoWindow infoWindow = marker.getToolTip(mapView);
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(), SpotInfo.class);
                intent.putExtra("Position", currentSpot.getSpotId());
                startActivity(intent);
                return true;
            }
        };
        infoWindow.setOnTouchListener(onTouchListener);
        marker.setToolTip(infoWindow);
        //TODO: Ändra här så att det blir rätt ikon för spots på kartan
        marker.setIcon(new Icon(this, Icon.Size.MEDIUM, "circle", "4caf50"));
        mapView.addMarker(marker);
        markers.add(marker);
        onTouchListeners.add(onTouchListener);
        Manager.getInstance().saveChanges(getSharedPreferences("PREFS", Context.MODE_PRIVATE));
    }

    @Override
    protected void onDestroy() {
        Manager.getInstance().saveChanges(getSharedPreferences("PREFS", Context.MODE_PRIVATE));
        super.onDestroy();
        if(locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
//            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//            String data = Manager.getInstance().export();
//            ClipData clip = ClipData.newPlainText("inobi-data", data);
//            clipboard.setPrimaryClip(clip);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareHelper(){
        if(Manager.getInstance().getList().size() != 0){
            mShareIntent = new Intent();
            mShareIntent.setAction(Intent.ACTION_SEND);
            mShareIntent.setType("text/plain");
            mShareIntent.putExtra(Intent.EXTRA_TEXT, Manager.getInstance().export());

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(mShareIntent);
            }
        }
    }
}