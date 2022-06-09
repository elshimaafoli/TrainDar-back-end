package com.points;

import com.appuser.AppUser;
import com.appuser.AppUserRepository;
import com.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PointsHistoryService {
    private final AppUserRepository appUserRepository;
    private final PointsHistoryRepository pointsHistoryRepository;
    //check the user have enough points or not
    public boolean haveEnoughPoints(Long id, Long amount) {
        AppUser userFrom = appUserRepository.findById(id).get();
        if (userFrom.getPoints() > amount)
            return true;
        return false;
    }

    //send points from user to another
    public void sendPoints(Long from, String emailto, Long amount) {
        AppUser userFrom, userTo;
        String details = "";
        userFrom = appUserRepository.findById(from).get();
        var existedUser = appUserRepository.findByEmail(emailto);
        if (existedUser.isEmpty()) {
            throw new IllegalStateException("There is no user with this Email");
        }
        userTo = existedUser.get();
        if (haveEnoughPoints(from, amount)) {
            userFrom.setPoints(userFrom.getPoints() - amount);
            userTo.setPoints(userTo.getPoints() + amount);
        } else {
            throw new IllegalStateException("User does not have enough points");
        }
        details = "Points sent as a gift";
        PointsHistory pointsHistoryFrom = new PointsHistory();
        pointsHistoryFrom.setFromName(userFrom.getName());
        pointsHistoryFrom.setToName(userTo.getName());
        pointsHistoryFrom.setAmount(amount);
        pointsHistoryFrom.setDetails(details);
        pointsHistoryFrom.setDate(LocalDateTime.now());
        userFrom.getPointsHistory().add(pointsHistoryFrom);
        pointsHistoryFrom.setAppUser(userFrom);
        appUserRepository.saveAndFlush(userFrom);
        pointsHistoryRepository.saveAndFlush(pointsHistoryFrom);
        PointsHistory pointsHistoryTo = new PointsHistory();
        pointsHistoryTo.setFromName(userFrom.getName());
        pointsHistoryTo.setToName(userTo.getName());
        pointsHistoryTo.setAmount(amount);
        pointsHistoryTo.setDetails(details);
        pointsHistoryTo.setDate(LocalDateTime.now());
        userFrom.getPointsHistory().add(pointsHistoryTo);
        details = "Points received as a gift";
        pointsHistoryTo.setDetails(details);
        userTo.getPointsHistory().add(pointsHistoryTo);
        pointsHistoryTo.setAppUser(userTo);
        appUserRepository.saveAndFlush(userTo);
        pointsHistoryRepository.saveAndFlush(pointsHistoryTo);
    }

    //increase and decrease user points
    public void points(Long id, String subject) {
        String details = "";
        AppUser user = appUserRepository.findById(id).get();
        PointsHistory pointsHistory = new PointsHistory();
        //Done : build massage
        if (subject == "Bad Share") {
            if (user.getPoints() - 2 >= 0) {
                user.setPoints(user.getPoints() - 2);
            } else {
                return;
                //throw new IllegalStateException("User does not have enough points");
            }
            details = "Points deducted for false location sharing";
            pointsHistory.setToName("TrainDar");
            pointsHistory.setFromName("YOU");
            pointsHistory.setAmount(2l);
        } else if (subject == "Good Share") {
            user.setPoints(user.getPoints() + 2);
            return;
            /*pointsHistory.setToName("YOU");
            pointsHistory.setFromName("TrainDar");
            pointsHistory.setAmount(2l);
            return;*/
        } else if (subject == "Up Coming Trains") {
            if (user.getPoints() - 3 >= 0) {
                user.setPoints(user.getPoints() - 3);
            } else {
                throw new IllegalStateException("User does not have enough points");
            }
            details = "Points deducted for using a TrainDar feature";
            pointsHistory.setToName("TrainDar");
            pointsHistory.setFromName("YOU");
            pointsHistory.setAmount(3l);
        } else if (subject == "Nearest Station") {
            if (user.getPoints() - 3 >= 0) {
                user.setPoints(user.getPoints() - 3);
            } else {
                throw new IllegalStateException("User does not have enough points");
            }
            details = "Points deducted for using a TrainDar feature";
            pointsHistory.setToName("TrainDar");
            pointsHistory.setFromName("YOU");
            pointsHistory.setAmount(3l);
        } else if (subject == "Search") {
            if (user.getPoints() - 3 >= 0) {
                user.setPoints(user.getPoints() - 3);
            } else {
                throw new IllegalStateException("User does not have enough points");
            }
            details = "Points deducted for using a TrainDar feature";
            pointsHistory.setToName("TrainDar");
            pointsHistory.setFromName("YOU");
            pointsHistory.setAmount(3l);
        } else if (subject == "Invitation") {
            user.setPoints(user.getPoints() + 40);
            details = "Points increased due to invitations";
            pointsHistory.setToName("YOU");
            pointsHistory.setFromName("TrainDar");
            pointsHistory.setAmount(40l);
        } else if (subject == "Gift") {
            user.setPoints(user.getPoints() + 20);
            details = "Points increased due to a gift";
            pointsHistory.setToName("YOU");
            pointsHistory.setFromName("TrainDar");
            pointsHistory.setAmount(20l);
        } else if (subject == "Registeration") {
            user.setPoints(user.getPoints() + 50);
            details = "Welcoming Gift";
            pointsHistory.setToName("YOU");
            pointsHistory.setFromName("TrainDar");
            pointsHistory.setAmount(50l);
        }
        pointsHistory.setDetails(details);
        pointsHistory.setDate(LocalDateTime.now());
        user.getPointsHistory().add(pointsHistory);
        pointsHistory.setAppUser(user);
        appUserRepository.saveAndFlush(user);
        pointsHistoryRepository.saveAndFlush(pointsHistory);
    }

    // buy
    public void buyPoints(Long to, Long amount) {
        String details = "";
        AppUser userBuy = appUserRepository.findById(to).get();
        userBuy.setPoints(userBuy.getPoints() + amount);
        details = "Points bought";
        PointsHistory pointsHistory = new PointsHistory();
        pointsHistory.setFromName("TrainDar");
        pointsHistory.setToName(userBuy.getName());
        pointsHistory.setAmount(amount);
        pointsHistory.setDetails(details);
        pointsHistory.setDate(LocalDateTime.now());
        userBuy.getPointsHistory().add(pointsHistory);
        appUserRepository.saveAndFlush(userBuy);
        pointsHistory.setAppUser(userBuy);
        pointsHistoryRepository.saveAndFlush(pointsHistory);
    }

}/*

 done :remove outliers after decrease their points,
 ,
todo : did we delete the user from shared users of specifid train?

 done :call points each cycle (good locations),
 ,
 controllers,
 ,
 done :throw exceptions,
 ,
 done :add user id to all urls,
 ,
 test
 ,
 2 how to add to points history

*/


