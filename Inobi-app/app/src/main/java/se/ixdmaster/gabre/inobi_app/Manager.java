package se.ixdmaster.gabre.inobi_app;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Robin on 2015-04-30.
 */
public class Manager implements SpotManager{

    private ArrayList<Spot> list;
    private Spot lastAddedSpot;

    private static Manager instance;

    private Manager(){
        list = new ArrayList<Spot>();
    }

    public static Manager getInstance(){
        if(instance == null)
            instance = new Manager();
        return instance;
    }

    public Spot getLastAddedSpot(){
        return lastAddedSpot;
    }

    @Override
    public int count() {
        return list.size();
    }

    @Override
    public Spot getSpot(int id) {
        return list.get(id);
    }

    @Override
    public void addSpot(Spot spot) {
        list.add(spot);
        lastAddedSpot = spot;
    }

    @Override
    public ArrayList<Spot> getList() {
        return list;
    }

    /**
     * Might be a bad idea to use this since the code is bad since it makes use of the spotID
     * to locate the spots in the list. Therefore the removed item is replaced with a dummy item
     * @param spot
     */
    @Override
    public void removeSpot(Spot spot) {
        list.remove(spot);
        for(int i = 0; i < list.size(); i++){
            Log.d("REM", "| " + list.get(i).convertToString() + " | before" );
            list.get(i).setSpotId(i);
            Log.d("REM", "| " + list.get(i).convertToString() + " | after");
        }
    }

    public void clearData(){
        list = new ArrayList<>();
    }

    @Override
    public String export() {
        StringBuilder sB = new StringBuilder();
        for(Spot s : list){
            sB.append(s.convertToString());
        }
        return sB.toString();
    }

    @Override
    public void saveChanges(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray data = new JSONArray();
        for (Spot spot : list){
            JSONObject item = new JSONObject();
            try{
                item.put("spotId", spot.getSpotId());
                item.put("name", spot.getName());
                item.put("lat", spot.getLat());
                item.put("lng", spot.getLng());
                item.put("time", spot.getTime());
                item.put("date", spot.getDate());
                item.put("userId", spot.getUserId());
                item.put("project", spot.getProject());
                item.put("peoplePresent", spot.getPeoplePresent());
                item.put("nrOfConv", spot.getNrOfConv());
                item.put("nrOfcollab", spot.getNrOfcollab());
                item.put("percentSitting", spot.getPercentSitting());
                item.put("flowCounter", spot.getFlowCounter());
                data.put(item);
            }
            catch (JSONException e) {}
        }
        editor.putString("DATA", data.toString());
        Log.d("JSON", data.toString());
        editor.commit();
    }
}
