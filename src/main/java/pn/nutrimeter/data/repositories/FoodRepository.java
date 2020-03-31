package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
}
