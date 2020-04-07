package pn.nutrimeter.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import pn.nutrimeter.data.models.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {

    private String authority;
}
