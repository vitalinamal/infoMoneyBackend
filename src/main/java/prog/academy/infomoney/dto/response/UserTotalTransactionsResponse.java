package prog.academy.infomoney.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserTotalTransactionsResponse(BigDecimal totalBalance,
                                            BigDecimal totalIncome,
                                            BigDecimal totalOutcome) {
}
