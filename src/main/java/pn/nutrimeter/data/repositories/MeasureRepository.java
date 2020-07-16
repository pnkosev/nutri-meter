package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.Measure;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, String> {
    Measure findByName(String name);
}
