package com.train;

import com.location.Location;
import com.points.PointsHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/train")
@CrossOrigin(origins = "*")
public class TrainController {
    private final TrainRepository trainRepository;
    private final TrainService trainService;
    private final PointsHistoryService pointsHistoryService;
    @GetMapping(path = {"/view-all"})
    public List<Long> getTrains() {
        return trainRepository.findAllOnlyID();
    }

    @GetMapping(path = {"/show-upcoming-trains"})
    public List<UpcomingTrain> showUpcomingTrains(@RequestParam("user-id")Long userId,@RequestParam("first-city") String firstCity,
                                                  @RequestParam("second-city") String secondCity){
        pointsHistoryService.points(userId,"Up Comming Trains");
        return trainService.getUpcomingTrains(firstCity, secondCity);
    }

//    @GetMapping(path ={"/{id}"})
//    public Train showTrainById(@PathVariable Long id){
//        return trainRepository.findById(id).get();//return train
//    }

    @GetMapping(path = {"/view"})
    public Location getTrainLocation(@RequestParam("user-id")Long userId,@RequestParam("train-id") Long trainId) {
        pointsHistoryService.points(userId,"Search");
        return trainService.findById(trainId);//return location
    }

    @PutMapping("/add-user")
    public String addUserToTrain(@RequestParam("train-id") Long trainId, @RequestParam("user-id") Long userId) {
        trainService.addUser(trainId, userId);
        return "Added";
    }

    @DeleteMapping("/delete-user")
    public String deleteUserFromTrain(@RequestParam("train-id") Long trainId, @RequestParam("user-id") Long userId){
        trainService.deleteUSer(trainId, userId);
        return "deleted";
    }

}