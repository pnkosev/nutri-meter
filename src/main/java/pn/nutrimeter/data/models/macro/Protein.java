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
@Table(name = "proteins")
public class Protein extends BaseMacroNutrient {

    @Column(name = "cystine")
    private Double cystine;

    @Column(name = "histidine")
    private Double histidine;

    @Column(name = "isoleucine")
    private Double isoleucine;

    @Column(name = "leucine")
    private Double leucine;

    @Column(name = "lysine")
    private Double lysine;

    @Column(name = "methionine")
    private Double methionine;

    @Column(name = "phenylalanine")
    private Double phenylalanine;

    @Column(name = "threonine")
    private Double threonine;

    @Column(name = "tryprophan")
    private Double tryprophan;

    @Column(name = "tyrosine")
    private Double tyrosine;

    @Column(name = "valine")
    private Double valine;

    @OneToMany(mappedBy = "proteins")
    private List<Food> foods;
}
