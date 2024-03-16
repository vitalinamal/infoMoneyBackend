package prog.academy.infomoney.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prog.academy.infomoney.dto.request.TransactionRequest;
import prog.academy.infomoney.dto.response.ProfileResponse;
import prog.academy.infomoney.dto.response.ProfileTransactionsResponse;
import prog.academy.infomoney.dto.response.TotalTransactionsResponse;
import prog.academy.infomoney.dto.response.TransactionResponse;
import prog.academy.infomoney.entity.Profile;
import prog.academy.infomoney.entity.Transaction;
import prog.academy.infomoney.enums.TransactionType;
import prog.academy.infomoney.exceptions.ApplicationException;
import prog.academy.infomoney.repository.ProfileRepository;
import prog.academy.infomoney.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Log4j2
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final ProfileRepository profileRepository;
    private final WalletService walletService;
    private final CategoryService categoryService;
    private final ProfileService profileService;

    @Transactional
    public void createTransaction(TransactionRequest request, Long profileId) {

        var profile = this.profileService.getProfileById(profileId);

        repository.save(Transaction.builder()
                .profile(profile)
                .type(TransactionType.valueOf(request.type()))
                .amount(request.amount())
                .description(request.description())
                .category(request.categoryId() != null ? categoryService.getCategoryById(request.categoryId()) : null)
                .wallet(request.walletId() != null ? walletService.getWalletById(request.walletId()) : null)
                .createdAt(convertToLocalDateTime(request))
                .build());
    }

    @Transactional
    public void updateTransaction(TransactionRequest request, Long profileId, Long transactionId) {

        var profile = this.profileService.getProfileById(profileId);

        var transaction = this.getTransaction(transactionId);

        if (!transaction.getProfile().equals(profile)) {
            throw new ApplicationException("This transaction doesn't belong to wanted user!");
        }

        updateTransactionFromRequest(request, transaction);

        repository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long profileId, Long transactionId) {

        var profile = this.profileService.getProfileById(profileId);

        var transaction = this.getTransaction(transactionId);

        if (!transaction.getProfile().equals(profile)) {
            throw new ApplicationException("This transaction doesn't belong to wanted user!");
        }

        profile.getTransactions().removeIf(t -> t.equals(transaction));
        this.profileRepository.save(profile);
    }

    private static void updateTransactionFromRequest(TransactionRequest request, Transaction transaction) {
        transaction.setAmount(request.amount());
        transaction.setType(TransactionType.valueOf(request.type()));
        transaction.setDescription(request.description());
        transaction.setCreatedAt(convertToLocalDateTime(request));
    }

    private static LocalDateTime convertToLocalDateTime(TransactionRequest request) {
        // Define the date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return LocalDate.parse(request.createdAt(), formatter).atStartOfDay();
    }

    public TotalTransactionsResponse getTotalTransactionsForUsers() {

        var income = repository.sunIncomeTransactions();
        var outcome = repository.sumOutcomeTransactions();

        var incomeValue = income == null ? BigDecimal.ZERO : BigDecimal.valueOf(income);
        var outcomeValue = outcome == null ? BigDecimal.ZERO : BigDecimal.valueOf(outcome);

        var profiles = this.profileService.getProfiles();

        return TotalTransactionsResponse.builder()
                .totalIncome(incomeValue)
                .totalOutcome(outcomeValue)
                .totalBalance(incomeValue.subtract(outcomeValue))
                .profiles(profiles.stream()
                        .map(p -> new ProfileResponse(p.getId(), p.getName()))
                        .toList())
                .build();

    }

    public ProfileTransactionsResponse getTotalTransactionsForProfile(Long profileId, Long walletId, Long categoryId) {

        var profile = this.profileService.getProfileById(profileId);

        var income = getIncome(profile, walletId, categoryId);
        var outcome = getOutcome(profile, walletId, categoryId);

        var incomeValue = income == null ? BigDecimal.ZERO : BigDecimal.valueOf(income);
        var outcomeValue = outcome == null ? BigDecimal.ZERO : BigDecimal.valueOf(outcome);

        var transactions = getAllByFilter(profile, walletId, categoryId);

        return ProfileTransactionsResponse.builder()
                .totalTransactionsStatus(this.getTotalTransactionsForUsers())
                .currentProfileName(profile.getName())
                .currentProfileId(profile.getId())
                .profileTotalIncome(incomeValue)
                .profileTotalOutcome(outcomeValue)
                .profileTotalBalance(incomeValue.subtract(outcomeValue))
                .profileTransactions(transactions.stream().map(t -> TransactionResponse.builder()
                        .id(t.getId())
                        .amount(t.getAmount())
                        .description(t.getDescription())
                        .type(t.getType())
                        .categoryName(t.getCategory() != null ? t.getCategory().getName() : null)
                        .walletName(t.getWallet() != null ? t.getWallet().getName() : null)
                        .createdAt(t.getCreatedAt())
                        .build()).toList())
                .build();

    }

    private List<Transaction> getAllByFilter(Profile profile, Long walletId, Long categoryId) {
        if (walletId != null && categoryId != null) {
            return repository.findAllByProfileAndWalletAndCategory(profile, walletId, categoryId);
        } else if (walletId != null) {
            return repository.findAllByProfileAndWallet(profile, walletId);
        } else if (categoryId != null) {
            return repository.findAllByProfileAndCategory(profile, categoryId);
        } else {
            return repository.findAllByProfile(profile);
        }
    }

    private Double getOutcome(Profile profile, Long walletId, Long categoryId) {
        if (walletId != null && categoryId != null) {
            return repository.sumProfileOutcomeTransactionsByWalletAndCategory(profile, walletId, categoryId);
        } else if (walletId != null) {
            return repository.sumProfileOutcomeTransactionsByWallet(profile, walletId);
        } else if (categoryId != null) {
            return repository.sumProfileOutcomeTransactionsByCategory(profile, categoryId);
        } else {
            return repository.sumProfileOutcomeTransactions(profile);
        }
    }

    private Double getIncome(Profile profile, Long walletId, Long categoryId) {
        if (walletId != null && categoryId != null) {
            return repository.sumProfileIncomeTransactionsByWalletAndCategory(profile, walletId, categoryId);
        } else if (walletId != null) {
            return repository.sumProfileIncomeTransactionsByWallet(profile, walletId);
        } else if (categoryId != null) {
            return repository.sumProfileIncomeTransactionsByCategory(profile, categoryId);
        } else {
            return repository.sumProfileIncomeTransactions(profile);
        }
    }

    private Transaction getTransaction(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ApplicationException(STR."Transaction doesn't exist for id:  \{id}"));
    }
}
