package pn.nutrimeter.service.services.api;

import pn.nutrimeter.service.models.RoleServiceModel;

import java.util.Set;

public interface RoleService {

    void seedRoles();

    Set<RoleServiceModel> getAllAuthority();

    RoleServiceModel getByAuthority(String authority);
}
