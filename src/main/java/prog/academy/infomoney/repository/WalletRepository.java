package prog.academy.infomoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prog.academy.infomoney.entity.Profile;
import prog.academy.infomoney.entity.Wallet;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w WHERE w.profile = :profile")
    List<Wallet> findAllByProfile(@Param("profile") Profile profile);

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Wallet w WHERE w.name = :name AND w.profile.id = :profileId AND w.id <> :walletId")
    boolean existsByProfileIdAndNameAndNotId(@Param("name") String name, @Param("profileId") Long profileId, @Param("walletId") Long walletId);

}
