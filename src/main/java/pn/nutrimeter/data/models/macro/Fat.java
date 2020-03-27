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
@Table(name = "lipids")
public class Fat extends BaseMacroNutrient {

    @Column(name = "monounsaturated")
    private Double monounsaturated;

    @Column(name = "polyunsaturated")
    private Double polyunsaturated;

    @Column(name = "omega_3")
    private Double omega3;

    @Column(name = "omega_6")
    private Double omega6;

    @Column(name = "saturated")
    private Double saturated;

    @Column(name = "trans_fats")
    private Double transFat;

    @Column(name = "cholesterol")
    private Double cholesterol;

    @OneToMany(mappedBy = "fats")
    private List<Food> foods;
}
