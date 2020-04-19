package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.MacroTarget;

import java.util.Optional;

@Repository
public interface MacroTargetRepository extends JpaRepository<MacroTarget, String> {

    Optional<MacroTarget> findByLifeStageGroupId(String id);

    @Query("FROM MacroTarget AS m LEFT JOIN m.users AS u WHERE u.id = :userId")
    MacroTarget findByUserId(@Param("userId") String id);
}
