package prog.academy.infomoney.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import prog.academy.infomoney.entity.Profile;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @Query("SELECT p FROM Profile p WHERE p.id = :id")
    @EntityGraph(attributePaths = {"transactions"})
    Optional<Profile> findByOwnId(Long id);

    boolean existsByName(String name);

}
