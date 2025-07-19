package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Ticket;
import org.example.entities.Train;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TrainService {

    ObjectMapper mapper = new ObjectMapper();
    private static final Path TRAIN_PATH = Paths.get("data", "trains.json");
    private List<Train> trainList;

    public TrainService() throws IOException {
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

    public List<Train> searchAllTrains(String source,String destination){
        List<Train> foundTrain=trainList.stream().filter(train->{
            Map<String,Integer> stationList=train.getStations();
            return stationList.containsKey(source)&&stationList.containsKey(destination)&&stationList.get(source)<stationList.get(destination);
        }).collect(Collectors.toList());
        return foundTrain.size()==0?null:foundTrain;
    }

    public Ticket bookTicket(Train train,String source,String destination,String dateOfTravel,String userId) throws Exception {

            Train fetchTrain = trainList.stream().filter(train2 -> {
                return train2.getTrainId().equals(train.getTrainId());
            }).findFirst().orElse(null);

            String ocuupiedSeat = "";
            if (fetchTrain != null) {
                List<List<Integer>> seats = fetchTrain.getSeats();
                for (int i = 0; i < seats.size(); i++) {
                    for (int j = 0; j < seats.get(i).size(); j++) {
                        if (seats.get(i).get(j) == 0) {
                            seats.get(i).set(j, 1);
                            ocuupiedSeat = ocuupiedSeat + i + j;
                            break;
                        }

                    }
                    if (!ocuupiedSeat.equals("")) break;
                }

            }
            else{
                throw new Exception("Sorry, Train not found");
            }
            if(ocuupiedSeat.equals("")){
                throw new Exception("Sorry, All Seats are reserved in this train");
            }



//        JsonNode root = mapper.readTree(new File(TRAIN_PATH));
//        ArrayNode arr = (ArrayNode) root.get("seats");
//        ObjectNode first = (ObjectNode) arr.get(0);
//        first.put("name", "Naya Name");



        saveTrainInfile();

        return new Ticket(UUID.randomUUID().toString(), userId,source,destination, dateOfTravel,fetchTrain,ocuupiedSeat,true);

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
      Train listTrain=trainList.stream().filter(train->train.getTrainId().equals(fetchTrain.getTrainId())).findFirst().orElse(null);
      listTrain.getSeats().get(i).set(j,0);
      saveTrainInfile();
      return;
    }
}
