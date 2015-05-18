package se.ixdmaster.gabre.inobi_app;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class About extends ActionBarActivity {

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
        setContentView(R.layout.activity_about);
        textSetup();
    }

    private void textSetup(){

        Typeface SSPLight = Typeface.createFromAsset(getAssets(), "SourceSansPro-Light.ttf");

        TextView t1 = (TextView) findViewById(R.id.textView_about_about);
        TextView t2 = (TextView) findViewById(R.id.textView_about_text);
        TextView t3 = (TextView) findViewById(R.id.textView_about_design);
        TextView t4 = (TextView) findViewById(R.id.textView_about_designName);
        TextView t5 = (TextView) findViewById(R.id.textView_about_programing);
        TextView t6 = (TextView) findViewById(R.id.textView_about_programmingName);
        TextView t7 = (TextView) findViewById(R.id.textView_about_sound);
        TextView t8 = (TextView) findViewById(R.id.textView_about_soundName);

        t1.setTypeface(SSPLight);
        t2.setTypeface(SSPLight);
        t3.setTypeface(SSPLight);
        t4.setTypeface(SSPLight);
        t5.setTypeface(SSPLight);
        t6.setTypeface(SSPLight);
        t7.setTypeface(SSPLight);
        t8.setTypeface(SSPLight);

        t1.setTextSize(MainActivity.TEXT_SIZE);
        t2.setTextSize(MainActivity.SMALL_TEXT_SIZE);
        t3.setTextSize(MainActivity.TEXT_SIZE);
        t4.setTextSize(MainActivity.SMALL_TEXT_SIZE);
        t5.setTextSize(MainActivity.TEXT_SIZE);
        t6.setTextSize(MainActivity.SMALL_TEXT_SIZE);
        t7.setTextSize(MainActivity.TEXT_SIZE);
        t8.setTextSize(MainActivity.SMALL_TEXT_SIZE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
