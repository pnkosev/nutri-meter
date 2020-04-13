package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.associations.DailyStoryFood;

@Repository
public interface DailyStoryFoodRepository extends JpaRepository<DailyStoryFood, String> {
}
