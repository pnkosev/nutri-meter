package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeasureCreateBindingModel {

    private String name;

    private Double equivalentInGrams;
}
