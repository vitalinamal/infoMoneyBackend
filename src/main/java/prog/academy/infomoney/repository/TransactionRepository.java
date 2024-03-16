package prog.academy.infomoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prog.academy.infomoney.entity.Profile;
import prog.academy.infomoney.entity.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'INCOME'")
    Double sunIncomeTransactions();

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = 'OUTCOME'")
    Double sumOutcomeTransactions();

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.type = 'INCOME'")
    Double sumProfileIncomeTransactions(@Param("profile") Profile profile);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.type = 'OUTCOME'")
    Double sumProfileOutcomeTransactions(@Param("profile") Profile profile);

    List<Transaction> findAllByProfile(Profile profile);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.wallet.id = :walletId AND t.category.id = :categoryId AND t.type = 'INCOME'")
    Double sumProfileIncomeTransactionsByWalletAndCategory(
            @Param("profile") Profile profile, @Param("walletId") Long walletId, @Param("categoryId") Long categoryId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.wallet.id = :walletId AND t.category.id = :categoryId AND t.type = 'OUTCOME'")
    Double sumProfileOutcomeTransactionsByWalletAndCategory(
            @Param("profile") Profile profile, @Param("walletId") Long walletId, @Param("categoryId") Long categoryId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.wallet.id = :walletId AND t.type = 'OUTCOME'")
    Double sumProfileOutcomeTransactionsByWallet(@Param("profile") Profile profile, @Param("walletId") Long walletId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.category.id = :categoryId AND t.type = 'OUTCOME'")
    Double sumProfileOutcomeTransactionsByCategory(@Param("profile") Profile profile, @Param("categoryId") Long categoryId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.wallet.id = :walletId AND t.type = 'INCOME'")
    Double sumProfileIncomeTransactionsByWallet(@Param("profile") Profile profile, @Param("walletId") Long walletId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.profile = :profile AND t.category.id = :categoryId AND t.type = 'INCOME'")
    Double sumProfileIncomeTransactionsByCategory(@Param("profile") Profile profile, @Param("categoryId") Long categoryId);

    @Query("SELECT t FROM Transaction t WHERE t.profile = :profile AND t.wallet.id = :walletId AND t.category.id = :categoryId")
    List<Transaction> findAllByProfileAndWalletAndCategory(@Param("profile") Profile profile, @Param("walletId") Long walletId, @Param("categoryId") Long categoryId);

    @Query("SELECT t FROM Transaction t WHERE t.profile = :profile AND t.wallet.id = :walletId")
    List<Transaction> findAllByProfileAndWallet(@Param("profile") Profile profile, @Param("walletId") Long walletId);

    @Query("SELECT t FROM Transaction t WHERE t.profile = :profile AND t.category.id = :categoryId")
    List<Transaction> findAllByProfileAndCategory(@Param("profile") Profile profile, @Param("categoryId") Long categoryId);
}
