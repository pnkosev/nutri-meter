package pn.nutrimeter.web.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserSimpleViewModel {

    private String id;

    private String username;

    private String email;

    private Set<RoleViewModel> authorities;
}
