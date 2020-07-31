package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Exercise;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.ExerciseRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.ExerciseServiceModel;
import pn.nutrimeter.service.services.api.ExerciseService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ExerciseServiceModel> getAllNonCustom() {
        return this.exerciseRepository
                .findAllByUserIsNull()
                .stream()
                .map(e -> this.modelMapper.map(e, ExerciseServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ExerciseServiceModel getById(String exerciseId) {
        Exercise exercise = this.exerciseRepository.findById(exerciseId).orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_EXERCISE_ID));
        return this.modelMapper.map(exercise, ExerciseServiceModel.class);
    }

    @Override
    public ExerciseServiceModel getByNameAndKcalBurnedPerHour(String name, Double kcalBurnedPerHour) {
        Optional<Exercise> optionalExercise = this.exerciseRepository.findByNameAndKcalBurnedPerHourAndUserIdNull(name, kcalBurnedPerHour);

        ExerciseServiceModel exerciseServiceModel = null;

        if (optionalExercise.isPresent()) {
            Exercise exercise = optionalExercise.get();

            exerciseServiceModel = this.modelMapper.map(exercise, ExerciseServiceModel.class);
        }

        return exerciseServiceModel;
    }

    @Override
    public ExerciseServiceModel create(ExerciseServiceModel model, String username) {
        Exercise exercise = this.modelMapper.map(model, Exercise.class);
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
        exercise.setUser(user);
        return this.modelMapper.map(this.exerciseRepository.saveAndFlush(exercise), ExerciseServiceModel.class);
    }
}
