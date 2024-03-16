package prog.academy.infomoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import prog.academy.infomoney.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByProfileId(Long profileId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.name = :name AND c.profile.id = :profileId AND c.id <> :categoryId")
    boolean existsByProfileIdAndNameAndNotId(@Param("name") String name, @Param("profileId") Long profileId, @Param("categoryId") Long categoryId);
}
