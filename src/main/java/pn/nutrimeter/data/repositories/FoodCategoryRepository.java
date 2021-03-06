package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.FoodCategory;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory, String> {
}
