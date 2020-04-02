package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.MicroTarget;

@Repository
public interface MicroTargetRepository extends JpaRepository<MicroTarget, String> {

    MicroTarget findByLifeStageGroupId(String id);
}
