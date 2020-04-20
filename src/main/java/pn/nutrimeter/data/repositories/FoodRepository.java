package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pn.nutrimeter.data.models.Food;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {

    @Query("SELECT f FROM Food AS f WHERE f.isCustom = false")
    List<Food> findAllNonCustom();

    @Query("SELECT f FROM Food AS f WHERE f.user.id = :userId")
    List<Food> findAllCustomOfUser(@Param("userId") String userId);
}
