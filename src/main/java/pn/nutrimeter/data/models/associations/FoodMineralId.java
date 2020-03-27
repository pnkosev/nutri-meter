package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.micro.Mineral;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class FoodMineralId implements Serializable {

    private Food food;

    private Mineral mineral;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodMineralId that = (FoodMineralId) o;
        return Objects.equals(food, that.food) &&
                Objects.equals(mineral, that.mineral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(food, mineral);
    }
}
