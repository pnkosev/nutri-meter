package pn.nutrimeter.data.models.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseMacroNutrient extends BaseEntity {

    protected BaseMacroNutrient() {}

    @Column(name = "kcal_per_gram")
    private Integer kcalPerGram;
}
