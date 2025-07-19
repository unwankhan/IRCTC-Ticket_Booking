package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Train {

    private String trainId;
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, String> stationTimes;
    //private Map<String, String> stationTimes;
    DayOfWeek dayOfWeek;
    Date startDate;
    private Map<String, Integer> stations;


    public Train(String trainId, String trainNo, List<List<Integer>> seats, Map<String, String> stationTimes,  Map<String, Integer> stations) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTimes = stationTimes;
        this.stations = stations;
    }

    public Train() {}

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void bookSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public void setStationTimes(Map<String, String> stationTimes) {
        this.stationTimes = stationTimes;
    }

    public void setStations( Map<String, Integer> stations) {
        this.stations = stations;
    }


    public String getTrainId() {
        return this.trainId;
    }

    public String getTrainNo() {
        return this.trainNo;
    }

    public List<List<Integer>> getSeats() {
        return this.seats;
    }

    public Map<String, String> getStationTimes() {
        return this.stationTimes;
    }

    public  Map<String, Integer>  getStations() {
        return stations;
    }
}
