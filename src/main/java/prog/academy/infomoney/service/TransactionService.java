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

    private final TransactionRepository transactionRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public void createTransaction(TransactionRequest request, Long profileId) {

        var profile = this.getProfile(profileId);

        transactionRepository.save(Transaction.builder()
                .profile(profile)
                .type(TransactionType.valueOf(request.type()))
                .amount(request.amount())
                .description(request.description())
                .createdAt(convertToLocalDateTime(request))
                .build());
    }

    @Transactional
    public void updateTransaction(TransactionRequest request, Long profileId, Long transactionId) {

        var profile = this.getProfile(profileId);

        var transaction = this.getTransaction(transactionId);

        if(!transaction.getProfile().equals(profile)) {
            throw new ApplicationException("This transaction doesn't belong to wanted user!");
        }

        updateTransactionFromRequest(request, transaction);

        transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long profileId, Long transactionId) {

        var profile = this.getProfile(profileId);

        var transaction = this.getTransaction(transactionId);

        if(!transaction.getProfile().equals(profile)) {
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

        var income = transactionRepository.sunIncomeTransactions();
        var outcome = transactionRepository.sumOutcomeTransactions();

        var incomeValue = income == null ? BigDecimal.ZERO : BigDecimal.valueOf(income);
        var outcomeValue = outcome == null ? BigDecimal.ZERO : BigDecimal.valueOf(outcome);

        var profiles = this.getProfiles();

        return TotalTransactionsResponse.builder()
                .totalIncome(incomeValue)
                .totalOutcome(outcomeValue)
                .totalBalance(incomeValue.subtract(outcomeValue))
                .profiles(profiles.stream()
                        .map(p -> new ProfileResponse(p.getId(), p.getName()))
                        .toList())
                .build();

    }

    public ProfileTransactionsResponse getTotalTransactionsForProfile(Long profileId) {

        var profile = this.getProfile(profileId);

        var income = transactionRepository.sumProfileIncomeTransactions(profile);
        var outcome = transactionRepository.sumProfileOutcomeTransactions(profile);

        var incomeValue = income == null ? BigDecimal.ZERO : BigDecimal.valueOf(income);
        var outcomeValue = outcome == null ? BigDecimal.ZERO : BigDecimal.valueOf(outcome);

        var transactions = transactionRepository.findAllByProfile(profile);

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
                        .createdAt(t.getCreatedAt())
                        .build()).toList())
                .build();

    }


    private List<Profile> getProfiles() {
        return this.profileRepository.findAll();
    }
    private Transaction getTransaction(Long id) {
        return transactionRepository
                .findById(id)
                .orElseThrow(() -> new ApplicationException("Transaction doesn't exist for id:  " + id));
    }

    private Profile getProfile(Long id) {
        return profileRepository
                .findById(id)
                .orElseThrow(() -> new ApplicationException("Current user doesn't have a profile with this id:  " + id));

    }
}
