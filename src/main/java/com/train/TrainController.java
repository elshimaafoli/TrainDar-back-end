package com.train;

import com.location.Location;
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

    @GetMapping(path = {"/view"})
    public List<Long> getTrains() {
        return trainRepository.findAllOnlyID();
    }

    @GetMapping(path = {"/show-upcoming-trains"})
    public List<UpcomingTrain> showUpcomingTrains(@RequestParam("first-city") String firstCity, @RequestParam("second-city") String secondCity){
        return trainService.getUpcomingTrains(firstCity, secondCity);
    }

    @GetMapping(path ={"/{id}"})
    public Train showTrainById(@PathVariable Long id){
        return trainRepository.findById(id).get();
    }

    @GetMapping(path = {"/view/{id}"})
    public Location getTrainLocation(@PathVariable Long id) {
        return trainService.findById(id);
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

    // to get next station of a train
    //TODO: path = {"/path/next"}
    //TODO: remove outliers
}