//package pn.nutrimeter.data.models.associations;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import pn.nutrimeter.data.models.DailyStory;
//import pn.nutrimeter.data.models.Food;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class DailyStoryFoodId implements Serializable {
//
//    private DailyStory dailyStory;
//
//    private Food food;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        DailyStoryFoodId that = (DailyStoryFoodId) o;
//        return Objects.equals(dailyStory, that.dailyStory) &&
//                Objects.equals(food, that.food);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(dailyStory, food);
//    }
//}
