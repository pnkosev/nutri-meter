package pn.nutrimeter.service.facades;

import java.util.Set;

public interface AuthenticationFacade {

    String getUsername();

    Set<String> getRoles();
}
