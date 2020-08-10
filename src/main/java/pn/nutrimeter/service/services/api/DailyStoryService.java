package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.DailyStoryServiceModel;

import java.time.LocalDate;

public interface DailyStoryService {

    DailyStoryServiceModel getByDateAndUserId(LocalDate date, String userId);
}
