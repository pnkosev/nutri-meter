package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel {

    private String Id;

    private String username;

    private String email;

    private Double totalKcalTarget;

    private Double kcalFromActivityLevel;

    private Double bmr;

    private Set<RoleServiceModel> authorities;
}
