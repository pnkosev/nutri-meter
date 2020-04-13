package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.errors.ErrorConstants;
import pn.nutrimeter.errors.UserNotFoundException;
import pn.nutrimeter.service.factories.daily_story.DailyStoryFoodFactory;
import pn.nutrimeter.service.models.DailyStoryNutrientServiceModel;
import pn.nutrimeter.service.models.DailyStoryServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DailyStoryServiceImpl implements DailyStoryService {

    private final DailyStoryRepository dailyStoryRepository;

    private final UserRepository userRepository;

    private final DailyStoryFoodFactory dailyStoryFoodFactory;

    private final ModelMapper modelMapper;

    public DailyStoryServiceImpl(DailyStoryRepository dailyStoryRepository, UserRepository userRepository, DailyStoryFoodFactory dailyStoryFoodFactory, ModelMapper modelMapper) {
        this.dailyStoryRepository = dailyStoryRepository;
        this.userRepository = userRepository;
        this.dailyStoryFoodFactory = dailyStoryFoodFactory;
        this.modelMapper = modelMapper;
    }

    @Override
    public DailyStoryServiceModel getByDateAndUserId(LocalDate date, String id) {
        Optional<DailyStory> dailyStoryOptional = this.dailyStoryRepository.findByDateAndUserId(date, id);

        DailyStory dailyStory;
        User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ErrorConstants.USER_ID_NOT_FOUND));

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

        List<DailyStoryNutrientServiceModel> dailyStoryFoodServiceModels = this.getModels(dailyStory);
        DailyStoryServiceModel dailyStoryServiceModel = this.modelMapper.map(dailyStory, DailyStoryServiceModel.class);
        dailyStoryServiceModel.setDailyStoryFoodAssociation(dailyStoryFoodServiceModels);

        this.reduceNutrientsFromListOfFoods(dailyStoryServiceModel);

        return dailyStoryServiceModel;
    }

    private List<DailyStoryNutrientServiceModel> getModels(DailyStory dailyStory) {
        List<DailyStoryNutrientServiceModel> newList = new ArrayList<>();
        return dailyStory.getDailyStoryFoodAssociation() == null
                ? newList
                : dailyStory.getDailyStoryFoodAssociation()
                .stream()
                .map(ds -> {
                    double percentage = ds.getGramsConsumed() / 100;
                    Food food = ds.getFood();
                    FoodServiceModel foodServiceModel = this.modelMapper.map(food, FoodServiceModel.class);
                    return dailyStoryFoodFactory.create(foodServiceModel, percentage, ds.getTimeOfDay());
                })
                .collect(Collectors.toList());
    }

    private void reduceNutrientsFromListOfFoods(DailyStoryServiceModel dailyStoryServiceModel) {
        List<DailyStoryNutrientServiceModel> dailyStoryFoodAssociation = dailyStoryServiceModel.getDailyStoryFoodAssociation();

        dailyStoryServiceModel.setKcal(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getKcal).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTotalProteins(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getTotalProteins).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setCysteine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getCysteine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setHistidine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getHistidine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setIsoleucine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getIsoleucine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setLeucine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getLeucine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setLysine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getLysine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setMethionine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getMethionine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setPhenylalanine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getPhenylalanine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setThreonine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getThreonine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTryptophan(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getTryptophan).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTyrosine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getTyrosine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setValine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getValine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTotalCarbohydrates(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getTotalCarbohydrates).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setFiber(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getFiber).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setStarch(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getStarch).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setSugars(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getSugars).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setAddedSugars(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getAddedSugars).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTotalLipids(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getTotalLipids).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setMonounsaturated(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getMonounsaturated).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setPolyunsaturated(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getPolyunsaturated).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setOmega3(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getOmega3).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setOmega6(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getOmega6).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setSaturated(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getSaturated).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setCholesterol(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getCholesterol).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminA(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminA).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB1(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminB1).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB2(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminB2).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB3(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminB3).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB5(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminB5).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB6(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminB6).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB12(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminB12).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setFolate(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getFolate).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminC(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminC).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminD(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminD).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminE(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminE).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminK(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getVitaminK).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setCalcium(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getCalcium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setCopper(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getCopper).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setIodine(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getIodine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setIron(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getIron).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setMagnesium(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getMagnesium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setManganese(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getManganese).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setPhosphorus(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getPhosphorus).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setPotassium(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getPotassium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setSelenium(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getSelenium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setSodium(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getSodium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setZinc(dailyStoryFoodAssociation.stream().map(DailyStoryNutrientServiceModel::getZinc).reduce(0.0, Double::sum));
    }
}
