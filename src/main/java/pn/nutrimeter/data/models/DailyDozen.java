package pn.nutrimeter.data.models;

import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "daily_dozens")
public class DailyDozen extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "servings_per_day")
    private Integer servingsPerDay;

    @Column(name = "equivalent_of_one_serving_in_grams")
    private Double servingInGrams;

    @ManyToMany(mappedBy = "dailyDozens")
    private List<Food> foods;
}
