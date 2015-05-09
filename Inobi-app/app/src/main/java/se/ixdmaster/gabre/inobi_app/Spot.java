package se.ixdmaster.gabre.inobi_app;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Robin on 2015-04-30.
 */
public class Spot {
    private int spotId, userId;
    private String name;
    private String date;
    private double lat,lng;

    private int peoplePresent = 0;
    private int nrOfConv = 0;
    private int nrOfcollab = 0;
    private int percentSitting = 0;
    private int flowCounter = 0;

    /**
     *
     * @param spotId, chronological order
     * @param name, name of the place
     * @param lat, latitude
     * @param lng, longitude
     * @param date, date in string format, pref YYYYMMDD HHMM
     * @param userId, static variable from user, else 0
     * @param peoplePresent
     * @param nrOfConv
     * @param nrOfcollab
     * @param percentSitting
     * @param flowCounter
     */
    public Spot (int spotId, String name, double lat, double lng, String date, int userId, int peoplePresent,
                 int nrOfConv, int nrOfcollab, int percentSitting, int flowCounter){
        this.spotId = spotId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
        this.userId = userId;
        this.peoplePresent = peoplePresent;
        this.nrOfConv = nrOfConv;
        this.nrOfcollab = nrOfcollab;
        this.percentSitting = percentSitting;
        this.flowCounter = flowCounter;
    }

    public String convertToString () {
        if(name != null){
            StringBuilder sB = new StringBuilder();
            sB.append(spotId);
            sB.append("\n");
            sB.append(name);
            sB.append("\n");
            sB.append(lat);
            sB.append("\n");
            sB.append(lng);
            sB.append("\n");
            sB.append(date);
            sB.append("\n");
            sB.append(peoplePresent);
            sB.append("\n");
            sB.append(nrOfConv);
            sB.append("\n");
            sB.append(nrOfcollab);
            sB.append("\n");
            sB.append(percentSitting);
            sB.append("\n");
            sB.append(flowCounter);
            sB.append("\n");
            sB.append("\n");
            return sB.toString();
        }
        return null;
    }

    public ArrayList<String> stringSpotInfoList(){
        ArrayList<String> list = new ArrayList<>();
        list.add(Integer.toString(peoplePresent));
        list.add(Integer.toString(nrOfConv));
        list.add(Integer.toString(nrOfcollab));
        list.add(Integer.toString(flowCounter));
        return list;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getPeoplePresent() {
        return peoplePresent;
    }

    public void setPeoplePresent(int peoplePresent) {
        this.peoplePresent = peoplePresent;
    }

    public int getNrOfConv() {
        return nrOfConv;
    }

    public void setNrOfConv(int nrOfConv) {
        this.nrOfConv = nrOfConv;
    }

    public int getNrOfcollab() {
        return nrOfcollab;
    }

    public void setNrOfcollab(int nrOfcollab) {
        this.nrOfcollab = nrOfcollab;
    }

    public int getPercentSitting() {
        return percentSitting;
    }

    public void setPercentSitting(int percentSitting) {
        this.percentSitting = percentSitting;
    }

    public int getFlowCounter() {
        return flowCounter;
    }

    public void setFlowCounter(int flowCounter) {
        this.flowCounter = flowCounter;
    }
}
