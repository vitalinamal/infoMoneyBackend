package prog.academy.infomoney.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record UserProfileTransactionsResponse(
        UserTotalTransactionsResponse userTransactionStatus,
        BigDecimal profileTotalBalance,
        BigDecimal profileTotalIncome,
        BigDecimal profileTotalOutcome,
        List<TransactionResponse> profileTransactions
) {
}
