package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.DailyStory;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyStoryRepository extends JpaRepository<DailyStory, String> {

    Optional<DailyStory> findByDateAndUserId(LocalDate date, String id);
}
