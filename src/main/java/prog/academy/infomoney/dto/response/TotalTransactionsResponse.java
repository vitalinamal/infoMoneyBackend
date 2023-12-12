package prog.academy.infomoney.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record TotalTransactionsResponse(BigDecimal totalBalance,
                                        BigDecimal totalIncome,
                                        BigDecimal totalOutcome,
                                        List<ProfileResponse> profiles) {
}
