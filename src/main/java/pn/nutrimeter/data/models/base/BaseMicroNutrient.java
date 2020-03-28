package pn.nutrimeter.data.models.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseMicroNutrient extends BaseEntity {

    protected BaseMicroNutrient() {}

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
