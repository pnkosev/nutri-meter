package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Exercise;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.UserNotFoundException;
import pn.nutrimeter.service.factories.daily_story.DailyStoryExerciseFactory;
import pn.nutrimeter.service.factories.daily_story.DailyStoryFoodFactory;
import pn.nutrimeter.service.models.*;
import pn.nutrimeter.service.services.api.DailyStoryService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DailyStoryServiceImpl implements DailyStoryService {

    private final DailyStoryRepository dailyStoryRepository;

    private final UserRepository userRepository;

    private final JdbcTemplate jdbcTemplate;

    private final DailyStoryFoodFactory dailyStoryFoodFactory;

    private final DailyStoryExerciseFactory dailyStoryExerciseFactory;

    private final ModelMapper modelMapper;

    public DailyStoryServiceImpl(
            DailyStoryRepository dailyStoryRepository,
            UserRepository userRepository,
            JdbcTemplate jdbcTemplate, DailyStoryFoodFactory dailyStoryFoodFactory,
            DailyStoryExerciseFactory dailyStoryExerciseFactory,
            ModelMapper modelMapper
    ) {
        this.dailyStoryRepository = dailyStoryRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.dailyStoryFoodFactory = dailyStoryFoodFactory;
        this.dailyStoryExerciseFactory = dailyStoryExerciseFactory;
        this.modelMapper = modelMapper;
    }

    /**
     * Getting a daily story by a given date and user ID
     *
     * @param date   given date
     * @param userId user's ID
     * @return DailyStoryServiceModel
     */
    @Override
    public DailyStoryServiceModel getByDateAndUserId(LocalDate date, String userId) {
        Optional<DailyStory> dailyStoryOptional = this.dailyStoryRepository.findByDateAndUserId(date, userId);

        DailyStory dailyStory;
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_ID_NOT_FOUND));

        if (dailyStoryOptional.isEmpty()) {
            dailyStory = new DailyStory();
            dailyStory.setDate(date);

            dailyStory.setDailyWeight(user.getWeight());

            dailyStory.setUser(user);
        } else {
            dailyStory = dailyStoryOptional.get();

            LocalDate currentDate = LocalDate.now();
            if (currentDate.equals(date) || date.isAfter(currentDate)) {
                dailyStory.setDailyWeight(user.getWeight());
            }
        }
        this.dailyStoryRepository.saveAndFlush(dailyStory);


        DailyStoryServiceModel dailyStoryServiceModel = getReducedNutrients(dailyStory.getId());
        dailyStoryServiceModel.setId(dailyStory.getId());
        dailyStoryServiceModel.setDailyWeight(dailyStory.getDailyWeight());
        dailyStoryServiceModel.setDate(dailyStory.getDate());

        List<DailyStoryFoodServiceModel> dailyStoryFoodServiceModels = this.getFoodModels(dailyStory);
        List<DailyStoryExerciseServiceModel> dailyStoryExerciseServiceModels = this.getExerciseModels(dailyStory);

        dailyStoryServiceModel.setDailyStoryFoodAssociation(dailyStoryFoodServiceModels);
        dailyStoryServiceModel.setDailyStoryExerciseAssociation(dailyStoryExerciseServiceModels);

        List<DailyStoryExerciseServiceModel> dailyStoryExerciseAssociation = dailyStoryServiceModel.getDailyStoryExerciseAssociation();
        dailyStoryServiceModel.setKcalBurned(dailyStoryExerciseAssociation.stream().map(e -> e.getKcalBurnedPerHour() * (e.getDuration() / 60)).reduce(0.0, Double::sum));

        return dailyStoryServiceModel;
    }

    private DailyStoryServiceModel getReducedNutrients(String dailyStoryId) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("usp_reduce_nutrients");

        Map<String, Object> inParamMap = new HashMap<String, Object>();
        inParamMap.put("daily_story_id", dailyStoryId);
        SqlParameterSource in = new MapSqlParameterSource(inParamMap);
        Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
        ArrayList list = (ArrayList) simpleJdbcCallResult.get("#result-set-1");
        Map result = (Map) list.get(0);

        return this.modelMapper.map(result, DailyStoryServiceModel.class);
    }

    // extracts a list of DailyStoryExerciseServiceModels from a DailyStory
    private List<DailyStoryExerciseServiceModel> getExerciseModels(DailyStory dailyStory) {
        List<DailyStoryExerciseServiceModel> newList = new ArrayList<>();
        return dailyStory.getDailyStoryFoodAssociation() == null
                ? newList
                : dailyStory.getDailyStoryExerciseAssociation()
                .stream()
                .map(association -> {
                    Exercise exercise = association.getExercise();
                    ExerciseServiceModel model = this.modelMapper.map(exercise, ExerciseServiceModel.class);
                    return dailyStoryExerciseFactory.create(association, model);
                })
                .collect(Collectors.toList());
    }

    // extracts a list of DailyStoryFoodServiceModel from a DailyStory
    private List<DailyStoryFoodServiceModel> getFoodModels(DailyStory dailyStory) {
        List<DailyStoryFoodServiceModel> newList = new ArrayList<>();
        return dailyStory.getDailyStoryFoodAssociation() == null
                ? newList
                : dailyStory.getDailyStoryFoodAssociation()
                .stream()
                .map(association -> {
                    Food food = association.getFood();
                    FoodServiceModel foodServiceModel = this.modelMapper.map(food, FoodServiceModel.class);
                    return dailyStoryFoodFactory.create(association, foodServiceModel);
                })
                .collect(Collectors.toList());
    }

}
