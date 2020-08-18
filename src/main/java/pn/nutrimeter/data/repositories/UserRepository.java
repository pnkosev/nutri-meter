package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.Role;
import pn.nutrimeter.data.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAllByAuthoritiesNotContaining(Role role);

    @Procedure(name = "usp_update_user")
    void updateUser(
            @Param(value = "user_id") String userId, @Param(value = "life_stage_group_id") String lifeStageGroupId
    );
}
