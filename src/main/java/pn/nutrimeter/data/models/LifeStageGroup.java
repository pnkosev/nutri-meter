package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;
import pn.nutrimeter.data.models.enums.Sex;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "life_stage_groups")
public class LifeStageGroup extends BaseEntity {

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "lower_age_bound")
    private Integer lowerAgeBound;

    @Column(name = "upper_age_bound")
    private Integer upperAgeBound;

    @OneToMany(mappedBy = "lifeStageGroup")
    private List<User> users;
}
