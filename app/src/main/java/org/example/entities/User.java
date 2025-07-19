package org.example.entities;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.util.UserServiceUtil;
public class User {
    private String name;
    private String password;
    private String email;
    private String hashedPassword;
    private List<Ticket> ticketsBooked;
    private String userId;

    public User(String name, String password, String email, String userId) {
        this.name = name;
        this.password =password;
        this.email = email;
        this.hashedPassword = UserServiceUtil.hashPassword(password);
        this.ticketsBooked = new ArrayList<Ticket>();
        this.userId = userId;
    }

public User(){

}
    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getPassword() {
        return this.password;
    }

    public String getEmail() {return this.email;}
    public String getHashedPassword() {return this.hashedPassword;}


    public List<Ticket> getTicketsBooked() {return this.ticketsBooked;}


    public String getUserId() {return this.userId;}

    public void printTicketsBooked(){
        for(Ticket ticket:ticketsBooked){
            ticket.printTicketDetails();
        }
    }

}
