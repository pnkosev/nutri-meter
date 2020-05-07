package pn.nutrimeter.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FoodDetailedViewModel {

    private String id;

    private String name;

    private boolean isFavorite;
}
