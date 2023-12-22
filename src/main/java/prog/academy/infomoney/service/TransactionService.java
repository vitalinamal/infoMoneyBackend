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
import prog.academy.infomoney.entity.Transaction;
import prog.academy.infomoney.enums.TransactionType;
import prog.academy.infomoney.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Log4j2
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProfileService profileService;

    @Transactional
    public void createTransaction(TransactionRequest request, Long profileId) {

        var profile = profileService.getProfileByName(profileId);

        transactionRepository.save(Transaction.builder()
                .profile(profile)
                .type(TransactionType.valueOf(request.type()))
                .amount(request.amount())
                .description(request.description())
                .createdAt(convertToLocalDateTime(request))
                .build());
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

        var profiles = profileService.getProfiles();

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

        var profile = profileService.getProfileByName(profileId);

        var income = transactionRepository.sumProfileIncomeTransactions(profile);
        var outcome = transactionRepository.sumProfileOutcomeTransactions(profile);

        var incomeValue = income == null ? BigDecimal.ZERO : BigDecimal.valueOf(income);
        var outcomeValue = outcome == null ? BigDecimal.ZERO : BigDecimal.valueOf(outcome);

        var transactions = transactionRepository.findAllByProfile(profile);

        return ProfileTransactionsResponse.builder()
                .totalTransactionsStatus(this.getTotalTransactionsForUsers())
                .currentProfileName(profile.getName())
                .profileTotalIncome(incomeValue)
                .profileTotalOutcome(outcomeValue)
                .profileTotalBalance(incomeValue.subtract(outcomeValue))
                .profileTransactions(transactions.stream().map(t -> TransactionResponse.builder()
                        .amount(t.getAmount())
                        .description(t.getDescription())
                        .type(t.getType())
                        .createdAt(t.getCreatedAt())
                        .build()).toList())
                .build();

    }
}
