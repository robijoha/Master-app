package se.ixdmaster.gabre.inobi_app;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Robin on 2015-04-30.
 */
public interface SpotManager {

    public int count();
    public Spot getSpot(int id);
    public void addSpot(Spot spot);
    public ArrayList<Spot> getList();
    public void removeSpot(Spot spot);
    public String export();
    public void saveChanges(SharedPreferences sharedPreferences);
}
