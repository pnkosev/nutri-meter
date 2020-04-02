package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pn.nutrimeter.data.models.enums.Sex;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
public class LifeStageGroupBindingModel {

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private Integer lowerAgeBound;

    private Integer upperAgeBound;
}
