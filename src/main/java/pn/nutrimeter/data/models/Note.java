package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notes")
public class Note extends BaseEntity {

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "daily_story", referencedColumnName = "id")
    private DailyStory dailyStory;
}
