package se.ixdmaster.gabre.inobi_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.SystemClock;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class NewUser extends ActionBarActivity {

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

        setContentView(R.layout.activity_new_user);

        MainActivity.firstTime = false;
        setup();
    }

    private void setup(){
        Typeface SSPLight = Typeface.createFromAsset(getAssets(), "SourceSansPro-Light.ttf");
        Typeface SSPRegular = Typeface.createFromAsset(getAssets(), "SourceSansPro-Regular.ttf");

        final EditText e1 = (EditText) findViewById(R.id.editText_newUser_name);
        e1.setTypeface(SSPLight);
        e1.setHintTextColor(0x552E2E2E);
        e1.setHint("namn");
        e1.setTextColor(0xFF2E2E2E);
        e1.setSingleLine();
        e1.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TITLE_SIZE);
        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setCursorVisible(true);
            }
        });

        e1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_NONE <= actionId && actionId <= EditorInfo.IME_ACTION_DONE) {
                    e1.setCursorVisible(false);
                }
                return false;
            }
        });

        final EditText e2 = (EditText) findViewById(R.id.editText_newUser_project);
        e2.setTypeface(SSPLight);
        e2.setHintTextColor(0x552E2E2E);
        e2.setHint("projekt");
        e2.setTextColor(0xFF2E2E2E);
        e2.setTextSize(TypedValue.COMPLEX_UNIT_SP, MainActivity.TITLE_SIZE);
        e2.setSingleLine();
        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setCursorVisible(true);
            }
        });

        e2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_NONE <= actionId && actionId <= EditorInfo.IME_ACTION_DONE) {
                    e2.setCursorVisible(false);
                    MainActivity.USER_ID = e1.getText().toString();
                    MainActivity.PROJECT = e2.getText().toString();
                    if (MainActivity.USER_ID.compareTo("") != 0 && MainActivity.PROJECT.compareTo("") != 0) {
                        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = settings.edit();
                        edit.putString("NAME", MainActivity.USER_ID);
                        edit.commit();
                        finish();
                    } else {
                        if (MainActivity.USER_ID.compareTo("") == 0 && MainActivity.PROJECT.compareTo("") == 0){
                            ImageView imageView = (ImageView) findViewById(R.id.imageView_newUser_name_wrong);
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_out);
                            imageView.setAnimation(animation);
                            imageView.startAnimation(animation);
                            ImageView imageView1 = (ImageView) findViewById(R.id.imageView_newUser_project_wrong);
                            imageView1.setAnimation(animation);
                            imageView1.startAnimation(animation);

                        }else if (MainActivity.USER_ID.compareTo("") == 0){
                            ImageView imageView = (ImageView) findViewById(R.id.imageView_newUser_name_wrong);
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_out);
                            imageView.setAnimation(animation);
                            imageView.startAnimation(animation);

                        }else if (MainActivity.PROJECT.compareTo("") == 0){
                            ImageView imageView = (ImageView) findViewById(R.id.imageView_newUser_project_wrong);
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_out);
                            imageView.setAnimation(animation);
                            imageView.startAnimation(animation);
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
//        Toast.makeText(getApplicationContext(), "No back for you", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_user, menu);
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
