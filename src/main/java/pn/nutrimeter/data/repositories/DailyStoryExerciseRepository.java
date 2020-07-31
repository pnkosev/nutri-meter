package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.associations.DailyStoryExercise;

@Repository
public interface DailyStoryExerciseRepository extends JpaRepository<DailyStoryExercise, String> {
}
