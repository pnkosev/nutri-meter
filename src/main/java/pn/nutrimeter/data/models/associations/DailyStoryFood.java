package pn.nutrimeter.data.models.associations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.models.Food;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "daily_stories_foods")
@IdClass(DailyStoryFoodId.class)
public class DailyStoryFood {

    @Column(name = "time_of_day")
    private Timestamp timeOfDay;

    @Id
    @ManyToOne
    @JoinColumn(name = "daily_story_id", referencedColumnName = "id")
    private DailyStory dailyStory;

    @Id
    @ManyToOne
    @JoinColumn(name = "food_id", referencedColumnName = "id")
    private Food food;
}
