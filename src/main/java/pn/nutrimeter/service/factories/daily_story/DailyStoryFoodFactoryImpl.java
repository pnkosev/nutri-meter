package pn.nutrimeter.service.factories.daily_story;

import org.springframework.stereotype.Service;
import pn.nutrimeter.service.models.DailyStoryFoodServiceModel;
import pn.nutrimeter.service.models.FoodServiceModel;

import java.sql.Timestamp;

@Service
public class DailyStoryFoodFactoryImpl implements DailyStoryFoodFactory {
    @Override
    public DailyStoryFoodServiceModel create(FoodServiceModel food, double gramsConsumedInPercentage, Timestamp timeOfDay) {
        DailyStoryFoodServiceModel dailyStoryFoodServiceModel = new DailyStoryFoodServiceModel();

        dailyStoryFoodServiceModel.setTimeOfDay(timeOfDay);
        dailyStoryFoodServiceModel.setName(food.getName());
        dailyStoryFoodServiceModel.setGramsConsumed(gramsConsumedInPercentage * 100);
        dailyStoryFoodServiceModel.setKcal(food.getKcalPerHundredGrams() * gramsConsumedInPercentage);
        dailyStoryFoodServiceModel.setTotalProteins(this.calcAmount(food.getTotalProteins(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setCysteine(this.calcAmount(food.getCysteine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setHistidine(this.calcAmount(food.getHistidine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setIsoleucine(this.calcAmount(food.getIsoleucine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setLeucine(this.calcAmount(food.getLeucine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setLysine(this.calcAmount(food.getLysine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setMethionine(this.calcAmount(food.getMethionine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setPhenylalanine(this.calcAmount(food.getPhenylalanine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setThreonine(this.calcAmount(food.getThreonine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setTryptophan(this.calcAmount(food.getTryptophan(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setTyrosine(this.calcAmount(food.getTyrosine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setValine(this.calcAmount(food.getValine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setTotalCarbohydrates(this.calcAmount(food.getTotalCarbohydrates(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setFiber(this.calcAmount(food.getFiber(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setStarch(this.calcAmount(food.getStarch(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setSugars(this.calcAmount(food.getSugars(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setAddedSugars(this.calcAmount(food.getAddedSugars(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setTotalLipids(this.calcAmount(food.getTotalLipids(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setMonounsaturated(this.calcAmount(food.getMonounsaturated(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setPolyunsaturated(this.calcAmount(food.getPolyunsaturated(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setOmega3(this.calcAmount(food.getOmega3(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setOmega6(this.calcAmount(food.getOmega6(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setSaturated(this.calcAmount(food.getSaturated(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setTransFats(this.calcAmount(food.getTransFats(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setCholesterol(this.calcAmount(food.getCholesterol(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminA(this.calcAmount(food.getVitaminA(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminB1(this.calcAmount(food.getVitaminB1(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminB2(this.calcAmount(food.getVitaminB2(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminB3(this.calcAmount(food.getVitaminB3(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminB5(this.calcAmount(food.getVitaminB5(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminB6(this.calcAmount(food.getVitaminB6(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminB12(this.calcAmount(food.getVitaminB12(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setFolate(this.calcAmount(food.getFolate(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminC(this.calcAmount(food.getVitaminC(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminD(this.calcAmount(food.getVitaminD(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminE(this.calcAmount(food.getVitaminE(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setVitaminK(this.calcAmount(food.getVitaminK(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setCalcium(this.calcAmount(food.getCalcium(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setCopper(this.calcAmount(food.getCopper(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setIodine(this.calcAmount(food.getIodine(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setIron(this.calcAmount(food.getIron(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setMagnesium(this.calcAmount(food.getMagnesium(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setManganese(this.calcAmount(food.getManganese(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setPhosphorus(this.calcAmount(food.getPhosphorus(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setPotassium(this.calcAmount(food.getPotassium(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setSelenium(this.calcAmount(food.getSelenium(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setSodium(this.calcAmount(food.getSodium(), gramsConsumedInPercentage));
        dailyStoryFoodServiceModel.setZinc(this.calcAmount(food.getZinc(), gramsConsumedInPercentage));

        return dailyStoryFoodServiceModel;
    }

    private double calcAmount(Double ingredient, double gramsConsumedInPercentage) {
        if (ingredient == null) {
            return 0.0;
        }
        return ingredient * gramsConsumedInPercentage;
    }
}
