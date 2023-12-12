package prog.academy.infomoney.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProfileTransactionsResponse(
        TotalTransactionsResponse totalTransactionsStatus,
        String currentProfileName,
        BigDecimal profileTotalBalance,
        BigDecimal profileTotalIncome,
        BigDecimal profileTotalOutcome,
        List<TransactionResponse> profileTransactions
) {
}
