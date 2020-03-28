package pn.nutrimeter.data.models.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseNutrientRDI extends BaseEntity {

    protected BaseNutrientRDI() {}

    @Column(name = "name")
    private String name;

    @Column(name = "recommended_daily_intake")
    private Double rdi;

    @Column(name = "upper_limit")
    private Double ul;
}
