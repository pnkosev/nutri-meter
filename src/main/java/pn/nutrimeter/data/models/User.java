package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "birthday")
    private LocalDate birthday;

    @OneToOne
    @JoinColumn(name = "diary_id", referencedColumnName = "id")
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "target_id", referencedColumnName = "id")
    private Target target;

    @OneToMany(mappedBy = "user")
    private List<Food> foods;

    @OneToMany(mappedBy = "user")
    private List<Recipe> recipes;
}
