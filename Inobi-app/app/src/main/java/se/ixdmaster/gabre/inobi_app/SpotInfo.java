package se.ixdmaster.gabre.inobi_app;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class SpotInfo extends ActionBarActivity {
    private Spot spot;

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

        setContentView(R.layout.activity_spot_info);

        Bundle b = getIntent().getExtras();
        int id = (int) b.get("Position");

        final Manager manager = Manager.getInstance();
        spot = manager.getSpot(id);

        textStylingAndInfo();
        buttonHelper();
    }

    private void buttonHelper() {
        Button close = (Button) findViewById(R.id.button_info_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button delete = (Button) findViewById(R.id.button_info_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Manager.getInstance().removeSpot(spot);
                MainActivity.REDRAW = true;
                finish();
            }
        });
    }

    private void textStylingAndInfo(){
        Typeface SSPLight = Typeface.createFromAsset(getAssets(), "SourceSansPro-Light.ttf");
        Typeface SSPRegular = Typeface.createFromAsset(getAssets(), "SourceSansPro-Regular.ttf");

        ArrayList<TextView> list = new ArrayList<>();
        ArrayList<String> list2 = spot.stringSpotInfoList();
        list.add((TextView) findViewById(R.id.textView_info_people));
        list.add((TextView) findViewById(R.id.textView_info_conv));
        list.add((TextView) findViewById(R.id.textView_info_collab));
        list.add((TextView) findViewById(R.id.textView_info_sitting));
        list.add((TextView) findViewById(R.id.textView_info_flow));
        list.add((TextView) findViewById(R.id.textView_info_people_numb));
        list.add((TextView) findViewById(R.id.textView_info_conv_numb));
        list.add((TextView) findViewById(R.id.textView_info_collab_numb));
        list.add((TextView) findViewById(R.id.textView_info_flow_numb));
        list.add((TextView) findViewById(R.id.textView_info_name));
        int j = 0;
        for (int i = 0; i < list.size(); i++){
            TextView t = list.get(i);
            if(i <= 8){
                t.setTypeface(SSPLight);
                t.setTextColor(0xFF2E2E2E);
                if(i > 4){
                    t.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.NUMB_SIZE);
                    t.setText(list2.get(j));
                    j++;
                }else{
                    t.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TEXT_SIZE);
                }
            }else{
                t.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TITLE_SIZE);
                t.setTextColor(0xFF2E2E2E);
                t.setTypeface(SSPLight);
                t.setText(spot.getName());
            }
        }

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar_info);
        seekBar.setEnabled(false);
        seekBar.setMax(7);
        seekBar.setProgress(spot.getPercentSitting());
        seekBar.setSecondaryProgress(spot.getPercentSitting());

        Button button = (Button) findViewById(R.id.button_info_delete);
        button.setTypeface(SSPLight);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spot_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
