package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.MacroTarget;

@Repository
public interface MacroTargetRepository extends JpaRepository<MacroTarget, String> {

    MacroTarget findByLifeStageGroupId(String id);
}
