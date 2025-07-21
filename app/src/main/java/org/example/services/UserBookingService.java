package org.example.services;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.util.UserServiceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


public class UserBookingService {
    private User user1;
    private static final Path USER_PATH = Paths.get("data", "users.json");
    // "src/main/resources/localDb/users.json";
    private ObjectMapper mapper ;
    private static List<User> userList;

    public User getUser() {
        return user1;
    }
    TrainService trainService;

    public void setUser(User user) {
        this.user1 = user;
    }

    public UserBookingService(User user){
        this.user1 = user;
    }

    public UserBookingService() throws IOException {
        // 1) ObjectMapper banao
        this.mapper = new ObjectMapper();
        // 2) JavaTimeModule register karo
        mapper.registerModule(new JavaTimeModule());
        // 3) (optional) dates timestamps mein na likho
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        trainService=new TrainService();
        try{
            userList=loadUser();
        }
       catch(Exception e){
            userList=new LinkedList<>();
            e.printStackTrace();
       }
    }

    private List<User> loadUser() throws IOException {
        // 1) ClassLoader से resource पाओ
        File usersFile = USER_PATH.toFile();
        if (!usersFile.exists()) {
            // अगर फाइल नहीं है तो खाली list return करो
            return new LinkedList<>();
        }
        return mapper.readValue(usersFile, new TypeReference<List<User>>() {});
    }

    public Optional<User> loginUser(String userId, String password){

        AtomicBoolean isUserIdCorrect= new AtomicBoolean(false);
        Optional<User> foundUser=userList.stream().filter(user->{
           if(user.getUserId().equals(userId)&& UserServiceUtil.checkPassword(password,user.getHashedPassword())) {
               return true;
           }
           else if(user.getUserId().equals(userId)){
               isUserIdCorrect.set(true);
           }
               return false;
        }).findFirst();
        if(foundUser.isPresent()){
            return foundUser;
        }
        else if(isUserIdCorrect.get()){
            System.out.println("Incorrect Password");
        }
        else if (!isUserIdCorrect.get()){
            System.out.println("UserId Not Found");
        }
        return foundUser;
    }



    public boolean signUpUser(User user){
        try {
            userList.add(user);
            saveUserInFile();
            return Boolean.TRUE;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }

    }

    private void saveUserInFile() throws IOException {
        // Backup
        try {
            Files.copy(Paths.get(USER_PATH.toUri()), Paths.get(USER_PATH + ".bak"), StandardCopyOption.REPLACE_EXISTING);

            // Save updated
            mapper.writeValue(new File(USER_PATH.toUri()), userList);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }







    public void showBookingDetails(Ticket ticket){
        ticket.printTicketDetails();
    }








    public Object[] getTicketDetails(Ticket ticket){
        return ticket.getTicketInfo();
    }








    public Train searchTrain(String source,String destination) throws IOException {
        return trainService.searchTrain(source,destination);
    }








   public List<Train> searchAllTrains(String source, String destination, LocalDateTime date) throws IOException {
        return trainService.searchAllTrains(source,destination,date);
    }







    public Ticket bookTicket(Train train,String source,String destination,String dateOfTravel) throws Exception {
        try {
            Ticket currBookedTicket = trainService.bookTicket(train, source, destination, dateOfTravel, user1.getUserId());
            if (currBookedTicket != null) {
                user1.getTicketsBooked().add(currBookedTicket);
            }
            saveUserInFile();
            return currBookedTicket;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }


    public void cancelTicket(Ticket ticket) throws IOException {
        trainService.cancelTicket(ticket);
        user1.getTicketsBooked().remove(ticket);
        saveUserInFile();
        }






    public Ticket fetchTicket(String PNR) throws IOException {
        return user1.getTicketsBooked().stream().filter(currTicket->  {
            return currTicket.getTicketId().equals(PNR);
        }).findFirst().orElse(null);
    }






    public void  handleTrainList(List<Train> trains,String source,String destination) throws IOException {
        TrainService trainService=new TrainService();
        trainService.handleTrainList(trains,source,destination);
        return;
    }

}
