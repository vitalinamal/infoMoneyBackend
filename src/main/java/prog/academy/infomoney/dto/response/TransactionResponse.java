package prog.academy.infomoney.dto.response;

import lombok.Builder;
import prog.academy.infomoney.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
        Long id,
        String description,
        BigDecimal amount,
        String categoryName,
        String walletName,
        TransactionType type,
        LocalDateTime createdAt
) {
}
