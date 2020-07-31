package pn.nutrimeter.service.services.api;

import java.time.LocalDate;

public interface DailyStoryExerciseService {

    void create(LocalDate date, String userId, String exerciseId, Double duration);

    void delete(String dailyStoryExerciseId);
}
