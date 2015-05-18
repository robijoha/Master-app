package se.ixdmaster.gabre.inobi_app;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CreateSpot extends ActionBarActivity {

    private double currentLat;
    private double currentLng;
    private String nameOne, nameTwo, nameThree;
    protected static boolean flag = false;
    private ArrayList<Touple> tList;


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

        setContentView(R.layout.activity_create_spot);

        Bundle b = getIntent().getExtras();
        try{
            currentLat = (double) b.get("Lat");
            currentLng = (double) b.get("Lng");
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        buttonHelper();
        makeCloseList();
        setUpDesign();

    }

    public void makeCloseList(){
        ArrayList<Spot> list = Manager.getInstance().getList();
        tList = new ArrayList<>();
        for (Spot spot : list){
            double d = calculateDistance(currentLat, currentLng, spot.getLat(), spot.getLng());
            if(d <= 100){
                tList.add(new Touple(d, spot));
            }
        }

        TextView t1 = (TextView) findViewById(R.id.text_close_one);
        TextView t2 = (TextView) findViewById(R.id.text_close_two);
        TextView t3 = (TextView) findViewById(R.id.text_close_three);
        Button b1 = (Button) findViewById(R.id.button_create_spot_one);
        Button b2 = (Button) findViewById(R.id.button_create_spot_two);
        Button b3 = (Button) findViewById(R.id.button_create_spot_three);


        if(tList.size() >= 0){
            tList = bubbleSort(tList);
            if(tList.size() >= 3){
                nameOne = tList.get(0).spot.getName();
                nameTwo = tList.get(1).spot.getName();
                nameThree = tList.get(2).spot.getName();
                t1.setText(nameOne);
                t2.setText(nameTwo);
                t3.setText(nameThree);
            }else if(tList.size() == 2){
                nameOne = tList.get(0).spot.getName();
                nameTwo = tList.get(1).spot.getName();
                t1.setText(nameOne);
                t2.setText(nameTwo);
                t3.setVisibility(View.INVISIBLE);
                b3.setVisibility(View.INVISIBLE);
            }else if(tList.size() == 1){
                nameOne = tList.get(0).spot.getName();
                t1.setText(nameOne);
                t2.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
                t3.setVisibility(View.INVISIBLE);
                b3.setVisibility(View.INVISIBLE);
            }else if(tList.size() == 0){
                t1.setVisibility(View.INVISIBLE);
                b1.setVisibility(View.INVISIBLE);
                t2.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
                t3.setVisibility(View.INVISIBLE);
                b3.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flag){
            finish();
            flag = false;
        }
    }

    public void buttonHelper(){
        Button create = (Button) findViewById(R.id.button_add_new_spot);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsertSpotData.class);
                intent.putExtra("Lat", currentLat);
                intent.putExtra("Lng" , currentLng);
                intent.putExtra("Name", "");
                startActivity(intent);
            }
        });

        Button close = (Button) findViewById(R.id.button_close_new_spot);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button first = (Button) findViewById(R.id.button_create_spot_one);
        Button second = (Button) findViewById(R.id.button_create_spot_two);
        Button third = (Button) findViewById(R.id.button_create_spot_three);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsertSpotData.class);
                intent.putExtra("Lat", tList.get(0).spot.getLat());
                intent.putExtra("Lng" , tList.get(0).spot.getLng());
                intent.putExtra("Name", nameOne);
                startActivity(intent);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsertSpotData.class);
                intent.putExtra("Lat", tList.get(1).spot.getLat());
                intent.putExtra("Lng" , tList.get(1).spot.getLng());
                intent.putExtra("Name", nameTwo);
                startActivity(intent);
            }
        });

        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InsertSpotData.class);
                intent.putExtra("Lat", tList.get(2).spot.getLat());
                intent.putExtra("Lng" , tList.get(2).spot.getLng());
                intent.putExtra("Name", nameThree);
                startActivity(intent);
            }
        });



    }

    public void setUpDesign(){

        Typeface SSPLight = Typeface.createFromAsset(getAssets(), "SourceSansPro-Light.ttf");

        TextView t1 = (TextView) findViewById(R.id.text_button_add_new);
        t1.setTypeface(SSPLight);
        t1.setTextColor(0xFF2E2E2E);
        t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TITLE_SIZE);

        TextView t2 = (TextView) findViewById(R.id.text_existing_spot_header);
        t2.setTypeface(SSPLight);
        t2.setTextColor(0xFF868686);
        t2.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TITLE_SIZE);

        TextView t3 = (TextView) findViewById(R.id.text_close_one);
        t3.setTypeface(SSPLight);
        t3.setTextColor(0xFF2E2E2E);
        t3.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TITLE_SIZE);

        TextView t4 = (TextView) findViewById(R.id.text_close_two);
        t4.setTypeface(SSPLight);
        t4.setTextColor(0xFF2E2E2E);
        t4.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TITLE_SIZE);

        TextView t5 = (TextView) findViewById(R.id.text_close_three);
        t5.setTypeface(SSPLight);
        t5.setTextColor(0xFF2E2E2E);
        t5.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TITLE_SIZE);

    }

    /**
     * Bubblesort with duplicate eliminator
     * @param list a list with touples
     * @return sorted list with the shortest distance first, all names are exclusive.
     */
    public ArrayList<Touple> bubbleSort(ArrayList<Touple> list){
        Touple temp;
        ArrayList<Touple> tList = list;
        if (tList.size() > 1){
            for (int x = 0; x < tList.size(); x++){
                for (int i=0; i < tList.size() - x - 1; i++) {
                    if ( compareTo(tList.get(i), tList.get(i+1))){
                        temp = tList.get(i);
                        if(temp.name.compareTo(tList.get(i+1).name) == 0){
                            tList.remove(i + 1); //Removing the doubles
                        }else{
                            tList.set(i,tList.get(i + 1));
                            tList.set(i + 1, temp);
                        }
                    }
                }
            }
        }
        return tList;
    }

    public boolean compareTo(Touple t, Touple p){
        if(t.distance >= p.distance){
            return true;
        }else{
            return false;
        }
    }

    private class Touple {
        private double distance;
        private Spot spot;
        private String name;
        public Touple(double distance, Spot spot){
            this.distance = distance;
            this.spot = spot;
            this.name = spot.getName();
        }
    }

    public double calculateDistance(double lat1, double long1, double lat2, double long2){
        double R = 6371000; // metres
        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double lat2lat1Rad = Math.toRadians(lat2 - lat1);
        double lon2lon1Rad = Math.toRadians(long2 - long1);
        double a = Math.sin(lat2lat1Rad/2) * Math.sin(lat2lat1Rad/2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(lon2lon1Rad/2) * Math.sin(lon2lon1Rad/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_spot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
