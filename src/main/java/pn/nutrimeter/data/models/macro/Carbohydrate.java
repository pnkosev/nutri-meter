package pn.nutrimeter.data.models.macro;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.base.BaseMacroNutrient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carbohydrates")
public class Carbohydrate extends BaseMacroNutrient {

    @Column(name = "fiber")
    private Double fiber;

    @Column(name = "starch")
    private Double starch;

    @Column(name = "sugars")
    private Double sugars;

    @Column(name = "net_carbohydrates")
    private Double netCarbohydrates;

    @OneToMany(mappedBy = "carbs")
    private List<Food> foods;
}
