package pn.nutrimeter.service.facades;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Override
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Set<String> getRoles() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }
}
