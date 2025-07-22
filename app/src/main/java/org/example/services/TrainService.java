package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.example.entities.Ticket;
import org.example.entities.Train;


import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

public class TrainService {

    ObjectMapper mapper ;
    private static final Path TRAIN_PATH = Paths.get("data", "trains.json");
    private static List<Train> trainList;

    public TrainService() throws IOException {
        // 1) ObjectMapper banao
        this.mapper = new ObjectMapper();
        // 2) JavaTimeModule register karo
        mapper.registerModule(new JavaTimeModule());
        // 3) (optional) dates timestamps mein na likho
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        trainList=loadTrain();
    }



    private List<Train> loadTrain() throws IOException {
        File usersFile = TRAIN_PATH.toFile();
        if (!usersFile.exists()) {
            // अगर फाइल नहीं है तो खाली list return करो
            return new LinkedList<>();
        }
        return mapper.readValue(usersFile, new TypeReference<List<Train>>() {});
    }

    public Train searchTrainByNumber(String number) throws IOException {
        Optional<Train> foundTrain=trainList.stream().filter(train->{
            return train.getTrainNo().equals(number);
        }).findFirst();
        return foundTrain.orElse(null);
    }

    public Train searchTrainById(String id) throws IOException {
        Optional<Train> foundTrain=trainList.stream().filter(train->{
            return train.getTrainId().equals(id);
        }).findFirst();
        return foundTrain.orElse(null);
    }

    public Train searchTrain(String source,String destination) throws IOException {
        Optional<Train> foundTrain=trainList.stream().filter(train->{
            Map<String,Integer> stationList=train.getStations();
            return stationList.containsKey(source)&&stationList.containsKey(destination)&&stationList.get(source)<stationList.get(destination);
        }).findFirst();
        return foundTrain.orElse(null);
    }

    public List<Train> searchAllTrains(String source, String destination, LocalDateTime date) throws IOException {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<Train> foundTrain=trainList.stream().filter(train->{
            Map<String,LocalDateTime> stationTimes=train.getStationTimes();
            return stationTimes.containsKey(source)&& stationTimes.containsKey(destination)&&
                    stationTimes.get(source).getDayOfWeek()==dayOfWeek&& stationTimes.get(destination).isAfter(stationTimes.get(source));
        }).collect(Collectors.toList());
        return foundTrain.isEmpty() ?null:foundTrain;
    }

    public Ticket bookTicket(Train train2,String source,String destination,String dateOfTravel,String userId) throws Exception {
            StringBuilder occupiedSeat= new StringBuilder();
            Train train=trainList.stream().filter(train3-> { return train3.getTrainId().equals(train2.getTrainId()); }).findFirst().orElse(null);
            boolean isConfirmed;
            if (train.getAvailSeat()>0){
                List<List<Integer>> seatStruct=train.getSeats();
                outer:
                for (int i=0;i<seatStruct.size();i++) {
                    for (int j=0;j<seatStruct.get(i).size();j++) {
                        if (seatStruct.get(i).get(j).equals(0)) {
                            seatStruct.get(i).set(j, 1);
                            occupiedSeat.append(i).append(j);
                           break outer;
                        }
                    }
                }
                train.setAvailSeat(train.getAvailSeat()-1);
                isConfirmed=true;
            }
            else{
                throw new Exception("seats not available");
            }
            saveTrainInfile();

            return new Ticket(UUID.randomUUID().toString(), userId,source,destination, dateOfTravel,train, occupiedSeat.toString(),isConfirmed);

    }




    private void saveTrainInfile() throws IOException {
        try {
            Files.copy(Paths.get(TRAIN_PATH.toUri()), Paths.get(TRAIN_PATH + ".bak"), StandardCopyOption.REPLACE_EXISTING);

            // Save updated
            mapper.writeValue(new File(TRAIN_PATH.toUri()), trainList);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }





    public void cancelTicket(Ticket ticket) throws IOException {
      Train fetchTrain=ticket.getTrain();
      List<List<Integer>> seat=fetchTrain.getSeats();
      int i=ticket.getSeat().charAt(0)-'0',j=ticket.getSeat().charAt(1)-'0';
      seat.get(i).set(j,0);
      fetchTrain.setAvailSeat(fetchTrain.getAvailSeat()+1);
      //Train listTrain=trainList.stream().filter(train->train.getTrainId().equals(fetchTrain.getTrainId())).findFirst().orElse(null);
      //listTrain.getSeats().get(i).set(j,0);
      saveTrainInfile();
      return;
    }





    public void  handleTrainList(List<Train> trains,String source,String destination)
            throws IOException {
        if (trains == null || trains.isEmpty()) {
            System.out.println("No trains found. Try again with correct source/destination station:\n\n");
        } else {
            char plural=trains.size()>1?'s':' ';
            System.out.println("\n"+trains.size()+" Train"+plural +" found.\n\n");
            int i=1;
            for (Train t : trains) {
                System.out.println("Serial No.: "+i++);
                System.out.println("Train ID: " + t.getTrainId());
                System.out.println("Train No: " + t.getTrainNo());
                System.out.println("Train Name: " + t.getTrainName());
                if (t.getAvailSeat()>0)
                System.out.println("Availability Status: "+t.getAvailSeat()+" Available");
                else
                System.out.println("Availability Status: "+t.getAvailSeat()*-1+" Waiting");
                System.out.println("Route:");
                int start = t.getStations().get(source);
                int end = t.getStations().get(destination);
                t.getStations().entrySet().stream()
                        .filter(e -> e.getValue() >= start && e.getValue() <= end)
                        .forEach(e -> System.out.println("  - " + e.getKey()));
                System.out.println("\n\n");
            }
        }
    }









}
