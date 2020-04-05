package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.errors.UserNotFoundException;
import pn.nutrimeter.service.factories.daily_story.DailyStoryFoodFactory;
import pn.nutrimeter.service.models.DailyStoryFoodServiceModel;
import pn.nutrimeter.service.models.DailyStoryServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
        User user = this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No such user found!"));

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

        List<DailyStoryFoodServiceModel> dailyStoryFoodServiceModels = this.getModels(dailyStory);
        DailyStoryServiceModel dailyStoryServiceModel = this.modelMapper.map(dailyStory, DailyStoryServiceModel.class);
        dailyStoryServiceModel.setDailyStoryFoodAssociation(dailyStoryFoodServiceModels);

        this.reduceNutrientsFromListOfFoods(dailyStoryServiceModel);

        return dailyStoryServiceModel;
    }

    private List<DailyStoryFoodServiceModel> getModels(DailyStory dailyStory) {
        List<DailyStoryFoodServiceModel> newList = new ArrayList<>();
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

    private DailyStoryServiceModel reduceNutrientsFromListOfFoods(DailyStoryServiceModel dailyStoryServiceModel) {
        List<DailyStoryFoodServiceModel> dailyStoryFoodAssociation = dailyStoryServiceModel.getDailyStoryFoodAssociation();

        dailyStoryServiceModel.setKcal(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getKcal).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTotalProteins(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getTotalProteins).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setCysteine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getCysteine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setHistidine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getHistidine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setIsoleucine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getIsoleucine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setLeucine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getLeucine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setLysine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getLysine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setMethionine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getMethionine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setPhenylalanine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getPhenylalanine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setThreonine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getThreonine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTryptophan(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getTryptophan).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTyrosine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getTyrosine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setValine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getValine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTotalCarbohydrates(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getTotalCarbohydrates).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setFiber(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getFiber).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setStarch(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getStarch).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setSugars(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getSugars).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setAddedSugars(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getAddedSugars).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setTotalLipids(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getTotalLipids).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setMonounsaturated(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getMonounsaturated).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setPolyunsaturated(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getPolyunsaturated).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setOmega3(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getOmega3).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setOmega6(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getOmega6).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setSaturated(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getSaturated).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setCholesterol(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getCholesterol).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminA(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminA).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB1(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminB1).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB2(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminB2).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB3(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminB3).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB5(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminB5).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB6(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminB6).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminB12(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminB12).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setFolate(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getFolate).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminC(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminC).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminD(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminD).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminE(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminE).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setVitaminK(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getVitaminK).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setCalcium(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getCalcium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setCopper(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getCopper).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setIodine(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getIodine).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setIron(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getIron).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setMagnesium(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getMagnesium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setManganese(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getManganese).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setPhosphorus(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getPhosphorus).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setPotassium(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getPotassium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setSelenium(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getSelenium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setSodium(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getSodium).reduce(0.0, Double::sum));
        dailyStoryServiceModel.setZinc(dailyStoryFoodAssociation.stream().map(DailyStoryFoodServiceModel::getZinc).reduce(0.0, Double::sum));

        return dailyStoryServiceModel;
    }
}
