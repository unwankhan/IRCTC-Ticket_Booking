package org.example.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Train {

    private String trainId;
    private String trainNo;
    private String trainName;
    private List<List<Integer>> seats;
    private int availSeat;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Map<String, LocalDateTime> stationTimes;
    private Map<String, Integer> stations;



    public Train(String trainId, String trainNo, List<List<Integer>> seats, Map<String, LocalDateTime> stationTimes,  Map<String, Integer> stations,int availSeat,String trainName) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTimes = stationTimes;
        this.stations = stations;
        this.availSeat = availSeat;
        this.trainName = trainName;
    }

    public Train() {}


    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainName() {
        return this.trainName;
    }



    public void setAvailSeat(int availSeat) {
        this.availSeat = availSeat;
    }
    public int getAvailSeat() {
        return this.availSeat;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void setStationTimes(Map<String, LocalDateTime> stationTimes) {
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

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, LocalDateTime> getStationTimes() {
        return this.stationTimes;
    }

    public  Map<String, Integer>  getStations() {
        return stations;
    }
}
