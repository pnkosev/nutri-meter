package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;
import pn.nutrimeter.data.models.enums.AgeCategory;
import pn.nutrimeter.data.models.enums.Sex;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "micro_targets")
public class MicroTarget extends BaseEntity {

    @Column(name = "life_stage_category_id")
    private String lifeStageCategoryId;

    @Column(name = "vitamin_a_rda")
    private Double vitaminARDA;

    @Column(name = "vitamin_a_ul")
    private Double vitaminAUL;

    @Column(name = "vitamin_b1_rda")
    private Double vitaminB1RDA;

    @Column(name = "vitamin_b1_ul")
    private Double vitaminB1UL;

    @Column(name = "vitamin_b2_rda")
    private Double vitaminB2RDA;

    @Column(name = "vitamin_b2_ul")
    private Double vitaminB2UL;

    @Column(name = "vitamin_b3_rda")
    private Double vitaminB3RDA;

    @Column(name = "vitamin_b3_ul")
    private Double vitaminB3UL;

    @Column(name = "vitamin_b5_rda")
    private Double vitaminB5RDA;

    @Column(name = "vitamin_b5_ul")
    private Double vitaminB5UL;

    @Column(name = "vitamin_b6_rda")
    private Double vitaminB6RDA;

    @Column(name = "vitamin_b6_ul")
    private Double vitaminB6UL;

    @Column(name = "vitaminB12_rda")
    private Double vitaminB12RDA;

    @Column(name = "vitamin_b12_ul")
    private Double vitaminB12UL;

    @Column(name = "folate_rda")
    private Double folateRDA;

    @Column(name = "folate_ul")
    private Double folateUL;

    @Column(name = "vitamin_c_rda")
    private Double vitaminCRDA;

    @Column(name = "vitamin_c_ul")
    private Double vitaminCUL;

    @Column(name = "vitamin_d_rda")
    private Double vitaminDRDA;

    @Column(name = "vitamin_d_ul")
    private Double vitaminDUL;

    @Column(name = "vitamin_k_rda")
    private Double vitaminKRDA;

    @Column(name = "vitamin_k_ul")
    private Double vitaminKUL;

    @Column(name = "calcium_rda")
    private Double calciumRDA;

    @Column(name = "calcium_ul")
    private Double calciumUL;

    @Column(name = "copper_rda")
    private Double copperRDA;

    @Column(name = "copper_ul")
    private Double copperUL;

    @Column(name = "iodine_rda")
    private Double iodineRDA;

    @Column(name = "iodine_ul")
    private Double iodineUL;

    @Column(name = "iron_rda")
    private Double ironRDA;

    @Column(name = "iron_ul")
    private Double ironUL;

    @Column(name = "magnesium_rda")
    private Double magnesiumRDA;

    @Column(name = "magnesium_ul")
    private Double magnesiumUL;

    @Column(name = "manganese_rda")
    private Double manganeseRDA;

    @Column(name = "manganese_ul")
    private Double manganeseUL;

    @Column(name = "potassium_rda")
    private Double potassiumRDA;

    @Column(name = "potassium_ul")
    private Double potassiumUL;

    @Column(name = "selenium_rda")
    private Double seleniumRDA;

    @Column(name = "selenium_ul")
    private Double seleniumUL;

    @Column(name = "sodium_rda")
    private Double sodiumRDA;

    @Column(name = "sodium_ul")
    private Double sodiumUL;

    @Column(name = "zinc_rda")
    private Double zincRDA;

    @Column(name = "zinc_ul")
    private Double zincUL;

    @Column(name = "water_rda")
    private Double waterRDA;

    @OneToMany(mappedBy = "microTarget")
    private List<User> users;
}
