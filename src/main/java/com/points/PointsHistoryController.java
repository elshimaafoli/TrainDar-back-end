package com.points;

import com.appuser.AppUser;
import com.appuser.AppUserRepository;
import com.appuser.AppUserService;
import com.train.UpcomingTrain;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/points")
@CrossOrigin(origins = "*")
public class PointsHistoryController {
    private AppUserService appUserService;
    private PointsHistoryService pointsHistoryService;

    @GetMapping(path = {"/send-points"})
    public void sendPoints(@RequestParam("user-id") Long userId, @RequestParam("email-to") String emailto, @RequestParam("amount") Long amount) {
        pointsHistoryService.sendPoints(userId, emailto, amount);
    }

    @GetMapping(path = {"/buy"})
    public void buyPoints(@RequestParam("user-id") Long userId, @RequestParam("amount") Long amount) {
        pointsHistoryService.buyPoints(userId, amount);
    }

    @GetMapping(path = {"/show-all"})
    public List<PointsHistory> show(@RequestParam("user-id") Long userId) {
        AppUser appUser = appUserService.findById(userId);
        return appUser.getPointsHistory();
    }
}
