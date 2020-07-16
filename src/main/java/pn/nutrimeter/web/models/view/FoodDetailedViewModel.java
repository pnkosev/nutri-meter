package pn.nutrimeter.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.service.models.MeasureServiceModel;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FoodDetailedViewModel {

    private String id;

    private String name;

    private boolean isFavorite;

    private List<MeasureServiceModel> measures;
}
