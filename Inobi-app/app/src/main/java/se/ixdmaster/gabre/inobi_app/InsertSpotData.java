package se.ixdmaster.gabre.inobi_app;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


public class InsertSpotData extends ActionBarActivity {

    private boolean counting = false;
    private boolean first = true;


    //Data for spot creation
    private String spotName;
    private int peoplePresent = 0;
    private int nrOfConv = 0;
    private int nrOfcollab = 0;
    private int percentSitting = 0;
    private int flowCounter = 0;
    private double currentLat = 0;
    private double currentLng = 0;


    //TODO:SEEKBAR COLORS!!!!!!


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

        setContentView(R.layout.activity_insert_spot_data);

        Bundle b = getIntent().getExtras();
        try{
            currentLat = (double) b.get("Lat");
            currentLng = (double) b.get("Lng");
            spotName = (String) b.get("Name");
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        touchHandler();
        flowCounter();
        textSetup();

    }

    private void textSetup(){
        Typeface SSPLight = Typeface.createFromAsset(getAssets(), "SourceSansPro-Light.ttf");
        Typeface SSPRegular = Typeface.createFromAsset(getAssets(), "SourceSansPro-Regular.ttf");

        ArrayList<TextView> list = new ArrayList<>();
        list.add((TextView) findViewById(R.id.textView_insert_people_present));
        list.add((TextView) findViewById(R.id.textView_insert_current_conv));
        list.add((TextView) findViewById(R.id.textView_insert_collab));
        list.add((TextView) findViewById(R.id.textView_insert_people_sitting));
        list.add((TextView) findViewById(R.id.textView_flow_counter));
        list.add((TextView) findViewById(R.id.textView_insert_people_numb));
        list.add((TextView) findViewById(R.id.textView_insert_conv_numb));
        list.add((TextView) findViewById(R.id.textView_insert_collab_numb));

        for (int i = 0; i < list.size(); i++){
            TextView t = list.get(i);
            t.setTypeface(SSPRegular);
            if(i > 4){
                t.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.NUMB_SIZE);
            }else{
                t.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TEXT_SIZE);
            }
        }

        //Edit text setup i.e. the name at the top
        final EditText e1 = (EditText) findViewById(R.id.editText_name);
        e1.setTypeface(SSPLight);
        e1.setHint("Namn på plats...");
        e1.setTextColor(0xFF2E2E2E);
        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setCursorVisible(true);
            }
        });

        e1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(EditorInfo.IME_ACTION_NONE <= actionId && actionId <= EditorInfo.IME_ACTION_DONE){
                    e1.setCursorVisible(false);
                }
                return false;
            }
        });

        if(spotName != null) {
            e1.setText(spotName);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            e1.setEnabled(false);
        }


    }

    private void touchHandler(){
        final TextView textNrPeople = (TextView) findViewById(R.id.textView_insert_people_numb);
        final TextView textNrConv = (TextView) findViewById(R.id.textView_insert_conv_numb);
        final TextView textNrCollab = (TextView) findViewById(R.id.textView_insert_collab_numb);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);



        Button save = (Button) findViewById(R.id.button_save_spot);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spotName == null){
                    EditText name = (EditText) findViewById(R.id.editText_name);
                    spotName = name.getText().toString();
                }
                percentSitting = seekBar.getProgress();
                createNewSpot();
                MainActivity.FLAG = true;
                CreateSpot.flag = true;
                finish();
            }
        });

        Button close = (Button) findViewById(R.id.button_insert_close);
        Button addPeople = (Button) findViewById(R.id.button_insertSpot_addPresent);
        Button removePeople = (Button) findViewById(R.id.button_insertSpot_decPresent);
        Button addConv = (Button) findViewById(R.id.button_insertSpot_addConv);
        Button removeConv = (Button) findViewById(R.id.button_insertSpot_decConv);
        Button addCollab = (Button) findViewById(R.id.button_insertSpot_addCollab);
        Button removeCollab = (Button) findViewById(R.id.button_insertSpot_decCollab);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View.OnClickListener peopleClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == findViewById(R.id.button_insertSpot_decPresent)) {
                    if (peoplePresent > 0) {
                        peoplePresent--;
                        textNrPeople.setText(Integer.toString(peoplePresent));
                    }
                }
                if(v == findViewById(R.id.button_insertSpot_addPresent)){
                    peoplePresent++;
                    textNrPeople.setText(Integer.toString(peoplePresent));
                }
            }
        };

        View.OnClickListener convClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == findViewById(R.id.button_insertSpot_decConv)) {
                    if (nrOfConv > 0) {
                        nrOfConv--;
                        textNrConv.setText(Integer.toString(nrOfConv));
                    }
                }
                if(v == findViewById(R.id.button_insertSpot_addConv)){
                    nrOfConv++;
                    textNrConv.setText(Integer.toString(nrOfConv));
                }
            }
        };

        View.OnClickListener collabClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == findViewById(R.id.button_insertSpot_decCollab)) {
                    if (nrOfcollab > 0) {
                        nrOfcollab--;
                        textNrCollab.setText(Integer.toString(nrOfcollab));
                    }
                }
                if(v == findViewById(R.id.button_insertSpot_addCollab)){
                    nrOfcollab++;
                    textNrCollab.setText(Integer.toString(nrOfcollab));
                }
            }
        };

        addPeople.setOnClickListener(peopleClick);
        removePeople.setOnClickListener(peopleClick);
        addConv.setOnClickListener(convClick);
        removeConv.setOnClickListener(convClick);
        addCollab.setOnClickListener(collabClick);
        removeCollab.setOnClickListener(collabClick);
    }

    private void flowCounter(){
        final ImageView black = (ImageView) findViewById(R.id.imageView_flow_black);
        final ImageView darkFull = (ImageView) findViewById(R.id.imageView_flow_darkFull);
        final ImageView darkHalf = (ImageView) findViewById(R.id.imageView_flow_darkHalf);
        final ImageView lightLeft = (ImageView) findViewById(R.id.imageView_flow_lightHalf_left);
        final ImageView lightRight = (ImageView) findViewById(R.id.imageView_flow_lightHalf_right);
        final Animation shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink);
        final TextView counter = (TextView) findViewById(R.id.textView_flow_counter);

        final RotateAnimation rotate1 = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate1.setDuration(MainActivity.TIME);
        rotate1.setInterpolator(new LinearInterpolator());

        final RotateAnimation rotate2 = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate2.setDuration(MainActivity.TIME);
        rotate2.setInterpolator(new LinearInterpolator());

        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lightRight.startAnimation(rotate1);
                black.setVisibility(View.INVISIBLE);
                Log.d("ANI", "shrink stop");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rotate1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lightRight.setVisibility(View.INVISIBLE);


                darkHalf.startAnimation(rotate2);
                Log.d("ANI", "rotate1");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rotate2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("ANI", "End of animation");
                lightLeft.setVisibility(View.INVISIBLE);
                darkHalf.setVisibility(View.INVISIBLE);
                counting = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Button flowButton = (Button) findViewById(R.id.button_flow_counter);

        flowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first){
                    black.startAnimation(shrink);
                    counter.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.NUMB_SIZE);
                    counting = true;
                    first = false;
                }

                if(counting){
                    counter.setText(Integer.toString(flowCounter));
                    flowCounter++;
                }
            }
        });

    }

    /*
    Om det är en ny spot så blir spotId 1 mer än mängden bara.
     */
    public void createNewSpot(){
        Manager manager = Manager.getInstance();
        manager.addSpot(new Spot(manager.count(), spotName, currentLat, currentLng, new Date().toString(), MainActivity.USER_ID,
                peoplePresent, nrOfConv, nrOfcollab, percentSitting, flowCounter));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insert_spot_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
