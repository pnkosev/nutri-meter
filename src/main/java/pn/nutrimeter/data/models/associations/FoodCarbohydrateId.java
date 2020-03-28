package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.macro.Carbohydrate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class FoodCarbohydrateId implements Serializable {

    private Food food;

    private Carbohydrate carbohydrate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodCarbohydrateId that = (FoodCarbohydrateId) o;
        return Objects.equals(food, that.food) &&
                Objects.equals(carbohydrate, that.carbohydrate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(food, carbohydrate);
    }
}
