package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.micro.Vitamin;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class FoodVitaminId implements Serializable {

    private Food food;

    private Vitamin vitamin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodVitaminId that = (FoodVitaminId) o;
        return Objects.equals(food, that.food) &&
                Objects.equals(vitamin, that.vitamin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(food, vitamin);
    }
}
