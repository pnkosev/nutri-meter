package pn.nutrimeter.service.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MeasureServiceModel {

    private String id;

    private String name;

    private Double equivalentInGrams;
}
