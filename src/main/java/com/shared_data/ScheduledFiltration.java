package com.shared_data;

import com.train.TrainRepository;
import com.train.TrainService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@AllArgsConstructor
@Component
public class ScheduledFiltration {
    private  TrainRepository trainRepository;
    private  TrainService trainService;
    @Scheduled(fixedRate = 30000)
    public void updateAllTrains(){
        for(var train : trainRepository.findAll()){
            trainService.updateLocation(train.getId());
        }
    }
}