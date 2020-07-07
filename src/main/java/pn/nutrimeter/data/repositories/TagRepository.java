package pn.nutrimeter.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pn.nutrimeter.data.models.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {
}
