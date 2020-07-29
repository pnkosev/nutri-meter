package pn.nutrimeter.data.models.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JoinCriteria {

    private String table;
    private String field;
    private String value;
}
