package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.enums.Sex;

@Getter
@Setter
@NoArgsConstructor
public class LifeStageGroupServiceModel {

    private String id;

    private Sex sex;

    private double lowerAgeBound;

    private double upperAgeBound;
}
