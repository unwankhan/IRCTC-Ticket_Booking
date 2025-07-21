package org.example.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Ticket {

    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private String dateOfTravel;
    private Train train;
    private String seat;
    boolean status;
    public Ticket(String ticketId, String userId, String source, String destination, String dateOfTravel,Train train, String seat,boolean status) {
        this.ticketId = ticketId.substring(0,10);
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
        this.seat = seat;
        this.status=status;
    }

    public  Ticket(){}

    public String getTicketId() {
        return ticketId;
    }
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateOfTravel() {
        return dateOfTravel;
    }

    public void setDateOfTravel(String dateOfTravel) {
        this.dateOfTravel = dateOfTravel;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public String getSeat() {
        return this.seat;
    }
    public void setSeat(String seat) {
        this.seat = seat;
    }


    @JsonIgnore
    public Object[] getTicketInfo(){
        return new Object[]{this.ticketId,this.userId,this.source,this.destination,this.dateOfTravel,this.train,this.seat};
    }


    public void printTicketDetails(){
        System.out.println("\n\n");
        System.out.println("Ticket Details");
        System.out.println("Ticket Id: "+this.ticketId);
        System.out.println("Train No.: "+this.train.getTrainNo());
        System.out.println("Ticket Id: "+this.train.getTrainId());
        System.out.println("Train Name: "+this.train.getTrainName());
        System.out.println("User Id: "+this.userId);
        System.out.println("Source: "+this.source);
        System.out.println("Destination: "+this.destination);
        System.out.println("Date: "+this.dateOfTravel);
        if (this.status)
        System.out.println("Seat: A"+this.seat);
        System.out.println("Status: "+(this.status?"Confirmed":"Waiting"));
        System.out.println("\n\n");
    }
}
