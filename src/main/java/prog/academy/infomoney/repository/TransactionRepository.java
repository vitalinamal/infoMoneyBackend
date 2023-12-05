package prog.academy.infomoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prog.academy.infomoney.entity.Profile;
import prog.academy.infomoney.entity.Transaction;
import prog.academy.infomoney.entity.User;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile.user = :user AND t.type = 'INCOME'")
    Double sumUserIncomeTransactions(@Param("user") User user);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile.user = :user AND t.type = 'OUTCOME'")
    Double sumUserOutcomeTransactions(@Param("user") User user);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.type = 'INCOME'")
    Double sumProfileIncomeTransactions(@Param("profile") Profile profile);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.type = 'OUTCOME'")
    Double sumProfileOutcomeTransactions(@Param("profile") Profile profile);

    List<Transaction> findAllByProfile(Profile profile);
}