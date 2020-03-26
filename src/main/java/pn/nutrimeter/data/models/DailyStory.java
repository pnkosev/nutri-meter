package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "daily_dtories")
public class DailyStory extends BaseEntity {

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "dairy_id", referencedColumnName = "id")
    private Diary diary;

    @ManyToMany
    @JoinTable(
            name = "daily_stories_foods",
            joinColumns = @JoinColumn(name = "daily_story_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "food_id", referencedColumnName = "id")
    )
    private List<Food> foods;

    @ManyToMany
    @JoinTable(
            name = "daily_stories_exercises",
            joinColumns = @JoinColumn(name = "daily_story_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id", referencedColumnName = "id")
    )
    private List<Exercise> exercises;
}
