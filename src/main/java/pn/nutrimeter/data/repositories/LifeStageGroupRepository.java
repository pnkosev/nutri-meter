package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.LifeStageGroup;
import pn.nutrimeter.data.models.enums.Sex;

@Repository
public interface LifeStageGroupRepository extends JpaRepository<LifeStageGroup, String> {

    @Query("FROM LifeStageGroup as l " +
            "WHERE l.sex = :sex " +
            "AND l.lowerAgeBound <= :age " +
            "AND l.upperAgeBound >= :age")
    LifeStageGroup findLifeStageGroupBySexAndAge(@Param("sex") Sex sex, @Param("age") double age);
}
