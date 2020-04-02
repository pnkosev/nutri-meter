package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.LifeStageGroup;

@Repository
public interface LifeStageGroupRepository extends JpaRepository<LifeStageGroup, String> {
}
