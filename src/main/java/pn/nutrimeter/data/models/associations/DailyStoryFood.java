package pn.nutrimeter.data.models.associations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "daily_stories_foods")
public class DailyStoryFood extends BaseEntity {

    @Column(name = "time_of_day")
    private Timestamp timeOfDay;

    @Column(name = "measure")
    private String measure;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "grams_consumed")
    private Double gramsConsumed;

    @ManyToOne
    @JoinColumn(name = "daily_story_id", referencedColumnName = "id")
    private DailyStory dailyStory;

    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;
}
