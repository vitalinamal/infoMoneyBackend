package prog.academy.infomoney.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prog.academy.infomoney.dto.request.TransactionRequest;
import prog.academy.infomoney.dto.response.TransactionResponse;
import prog.academy.infomoney.dto.response.UserProfileTransactionsResponse;
import prog.academy.infomoney.dto.response.UserTotalTransactionsResponse;
import prog.academy.infomoney.entity.Transaction;
import prog.academy.infomoney.entity.User;
import prog.academy.infomoney.enums.TransactionType;
import prog.academy.infomoney.repository.TransactionRepository;

import java.math.BigDecimal;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ProfileService profileService;

    @Transactional
    public void createTransaction(TransactionRequest request, String profileName, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        var profile = profileService.getProfileByName(profileName, user.getId());

        transactionRepository.save(Transaction.builder()
                .profile(profile)
                .type(TransactionType.valueOf(request.type()))
                .amount(request.amount())
                .description(request.description())
                .build());
    }

    public UserTotalTransactionsResponse getTotalTransactionsForUsers(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        var income = transactionRepository.sumUserIncomeTransactions(user);
        var outcome = transactionRepository.sumUserOutcomeTransactions(user);

        var incomeValue = income == null ? BigDecimal.ZERO : BigDecimal.valueOf(income);
        var outcomeValue = outcome == null ? BigDecimal.ZERO : BigDecimal.valueOf(outcome);

        return UserTotalTransactionsResponse.builder()
                .totalIncome(incomeValue)
                .totalOutcome(outcomeValue)
                .totalBalance(incomeValue.subtract(outcomeValue))
                .build();

    }

    public UserProfileTransactionsResponse getTotalTransactionsForUserProfile(String profileName, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        var profile = profileService.getProfileByName(profileName, user.getId());

        var income = transactionRepository.sumProfileIncomeTransactions(profile);
        var outcome = transactionRepository.sumProfileOutcomeTransactions(profile);

        var incomeValue = income == null ? BigDecimal.ZERO : BigDecimal.valueOf(income);
        var outcomeValue = outcome == null ? BigDecimal.ZERO : BigDecimal.valueOf(outcome);

        var transactions = transactionRepository.findAllByProfile(profile);

        return UserProfileTransactionsResponse.builder()
                .userTransactionStatus(this.getTotalTransactionsForUsers(connectedUser))
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
