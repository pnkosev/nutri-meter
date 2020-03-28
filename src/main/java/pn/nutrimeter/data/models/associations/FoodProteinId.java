package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.macro.Protein;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class FoodProteinId implements Serializable {

    private Food food;

    private Protein protein;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodProteinId that = (FoodProteinId) o;
        return Objects.equals(food, that.food) &&
                Objects.equals(protein, that.protein);
    }

    @Override
    public int hashCode() {
        return Objects.hash(food, protein);
    }
}
