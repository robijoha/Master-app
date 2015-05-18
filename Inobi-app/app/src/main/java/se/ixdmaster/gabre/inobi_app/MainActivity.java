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
import android.util.Log;
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
    public static final int TEXT_SIZE = 24;
    public static final int NUMB_SIZE = 30;
    public static final int TITLE_SIZE = 30;
    public static final int SMALL_TEXT_SIZE = 16;

    protected static String USER_ID = "";
    protected static String PROJECT = "";
    protected static boolean FLAG = false;
    protected static boolean REDRAW = false;
    protected static boolean firstTime = true;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private Location myLocation;
    private MapView mapView;
    private ArrayList<Marker> markers;
    private ShareActionProvider mShareActionProvider;

    //TODO: Separera flera punkter på kartan som har samma koordinater?
    //TODO: Ljud?
    //TODO: Save, delete knapp!?

    //TODO: xml för insert spot, ny data input, samma för spot_info?
    //TODO: diskutera med erik och martin om hur det ska se ut.
    //TODO: Vibrationsfeedback?


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
        markers = new ArrayList<>();
        locationHelper();
        setupMap();
        buttonHandler();
    }

    /**
     * Load data and places all saved markers on the map.
     */
    private void loadData(){
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
                            data.getString("time"),
                            data.getString("date"),
                            data.getString("userId"),
                            data.getString("project"),
                            data.getInt("peoplePresent"),
                            data.getInt("nrOfConv"),
                            data.getInt("nrOfcollab"),
                            data.getInt("percentSitting"),
                            data.getInt("flowCounter"));
                    manager.addSpot(spot);
                    addMarkerWithListener(spot);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        shareHelper();
    }

    /**
     * Creates the MapView and sets the mapView listener so long clicks can be handled on the map.
     */
    private void setupMap (){
        mapView = (MapView) findViewById(R.id.mapview); // locates and initiates the mapview.
        /*
        UserLocationOverlay myLocationOverlay = new UserLocationOverlay(new GpsLocationProvider(getApplicationContext()) , mapView);
        myLocationOverlay.setOverlayCircleColor(0xFF909090);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.setDrawAccuracyEnabled(true);
        mapView.getOverlays().add(myLocationOverlay);
        */
        LatLng chalmers = new LatLng(57.689473, 11.976731);
        mapView.setCenter(chalmers);
        mapView.setZoom(17);
        mapView.setMaxZoomLevel(22);
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

    /**
     * The method creates all the buttonhandlers for this view.
     */
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
            Log.d("GPS", "Pause");
        }
        saveHelper();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        USER_ID = settings.getString("NAME", "");
        PROJECT = settings.getString("PROJECT", "");
        if(USER_ID.compareTo("") == 0){
            deleteHelper();
            Intent intent = new Intent(this, NewUser.class);
            startActivity(intent);
        }else{
            loadData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (Marker m : markers) { // first line
            mapView.removeMarker(m);    //Clears the map from the old markers to update from the latest version of the manager
        }
        markers.clear(); // last line
        for (Spot s : Manager.getInstance().getList()) {
            addMarkerWithListener(s);
        }
        mapView.closeCurrentTooltip();
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        shareHelper();
    }

    /**
     * Adds a marker to the map. In addition to this, the marker is saved in a list
     * and the created onTouchListener is also saved. This is so that the map can be
     * totally cleared later.
     * @param spot is the spot which should be added to the map.
     */
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
        marker.setIcon(new Icon(getResources().getDrawable(R.drawable.pin_54_96))); // Set the icon for markers
        mapView.addMarker(marker);
        markers.add(marker);
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
            return true;
        }

        if (id == R.id.delete) {
            buildAlertDeleteALl();
            return true;
        }

        if (id == R.id.new_project){
            buildNewProjectDialog();
            return true;
        }

        if (id == R.id.about) {
            Intent intent = new Intent(getApplicationContext(), About.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets up how to share the data from the application.
     */
    private void shareHelper(){
        if(Manager.getInstance().getList().size() != 0){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, Manager.getInstance().export());

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(shareIntent);
            }
        }
    }

    /**
     * Private method for saving since it was more before, now it's just looks cleaner. :D
     */
    private void saveHelper(){
        SharedPreferences sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NAME", USER_ID);
        editor.putString("PROJECT", PROJECT);
        editor.commit();
        Manager.getInstance().saveChanges(sharedPreferences);
    }

    private void deleteHelper(){
        Manager.getInstance().clearData();
        for(Marker m : markers){
            mapView.removeMarker(m);
        }
        markers.clear();
    }

    /**
     * Builds the alert dialog for deleting all spots.
     */
    private void buildAlertDeleteALl() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rensa data");
        builder.setMessage("Har du tänkt igenom det här beslutet?")
                .setCancelable(false)
                .setPositiveButton("Du är inte min pappa!", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        deleteHelper();
                    }
                })
                .setNegativeButton("Kanske inte, nej", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildNewProjectDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nytt projekt");
        builder.setMessage("Om du skapar ett nytt projekt rensas all tidigare data. Fortsätt?")
                .setCancelable(false)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        deleteHelper();
                        Intent intent = new Intent(getApplicationContext(), NewUser.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Men, nej", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Builds the alert dialog if the app i started without gps.
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS");
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
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}