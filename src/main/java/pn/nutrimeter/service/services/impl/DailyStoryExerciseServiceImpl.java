package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Exercise;
import pn.nutrimeter.data.models.associations.DailyStoryExercise;
import pn.nutrimeter.data.repositories.DailyStoryExerciseRepository;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.ExerciseRepository;
import pn.nutrimeter.error.DailyStoryNotFoundException;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.services.api.DailyStoryExerciseService;

import java.time.LocalDate;

@Service
public class DailyStoryExerciseServiceImpl implements DailyStoryExerciseService {

    private final DailyStoryExerciseRepository dailyStoryExerciseRepository;

    private final DailyStoryRepository dailyStoryRepository;

    private final ExerciseRepository exerciseRepository;

    private final ModelMapper modelMapper;

    public DailyStoryExerciseServiceImpl(DailyStoryExerciseRepository dailyStoryExerciseRepository, DailyStoryRepository dailyStoryRepository, ExerciseRepository exerciseRepository, ModelMapper modelMapper) {
        this.dailyStoryExerciseRepository = dailyStoryExerciseRepository;
        this.dailyStoryRepository = dailyStoryRepository;
        this.exerciseRepository = exerciseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(LocalDate date, String userId, String exerciseId, Double duration) {
        DailyStoryExercise dailyStoryExercise = new DailyStoryExercise();

        DailyStory dailyStory = this.dailyStoryRepository.findByDateAndUserId(date, userId).orElseThrow(() -> new DailyStoryNotFoundException(ErrorConstants.DAILY_STORY_NOT_FOUND));
        Exercise exercise = this.exerciseRepository.findById(exerciseId).orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_EXERCISE_ID));

        dailyStoryExercise.setDuration(duration);
        dailyStoryExercise.setDailyStory(dailyStory);
        dailyStoryExercise.setExercise(exercise);

        this.dailyStoryExerciseRepository.saveAndFlush(dailyStoryExercise);
    }

    @Override
    public void delete(String dailyStoryExerciseId) {
        // TODO
        DailyStoryExercise dailyStoryExercise = this.dailyStoryExerciseRepository.findById(dailyStoryExerciseId).get();
        this.dailyStoryExerciseRepository.delete(dailyStoryExercise);
    }
}
