package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.Exercise;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, String> {

    List<Exercise> findAllByUserIsNull();

    Optional<Exercise> findByNameAndKcalBurnedPerHourAndUserIdNull(String name, Double kcalBurnedPerHour);
}
