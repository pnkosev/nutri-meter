package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Role;
import pn.nutrimeter.data.repositories.RoleRepository;
import pn.nutrimeter.service.models.RoleServiceModel;
import pn.nutrimeter.service.services.api.RoleService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedRoles() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.saveAndFlush(new Role("ADMIN"));
            this.roleRepository.saveAndFlush(new Role("USER"));
        }
    }

    @Override
    public Set<RoleServiceModel> getAllAuthority() {
        return this.roleRepository.findAll()
                .stream()
                .map(a -> this.modelMapper.map(a, RoleServiceModel.class))
                .collect(Collectors.toSet());
    }

    @Override
    public RoleServiceModel getByAuthority(String authority) {
        return this.modelMapper.map(this.roleRepository.findByAuthority(authority), RoleServiceModel.class);
    }
}
