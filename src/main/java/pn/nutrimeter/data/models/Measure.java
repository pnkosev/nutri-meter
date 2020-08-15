package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "measures")
public class Measure extends BaseEntity {

    @Column(name = "name", nullable = false, length = 2)
    private String name;

    @Column(name = "equivalent_in_grams", nullable = false, precision = 4, scale = 2)
    private Double equivalentInGrams;

    @ManyToMany(mappedBy = "measures")
    private List<Food> foods;
}
