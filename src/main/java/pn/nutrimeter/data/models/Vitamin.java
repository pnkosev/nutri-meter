package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.associations.FoodVitamin;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vitamins")
public class Vitamin extends BaseEntity {

    private String name;

    private String description;

    @OneToMany(mappedBy = "vitamin")
    private List<FoodVitamin> foodVitaminAssociation;
}
