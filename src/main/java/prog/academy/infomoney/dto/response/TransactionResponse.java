package prog.academy.infomoney.dto.response;

import lombok.Builder;
import prog.academy.infomoney.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
        String description,
        BigDecimal amount,
        TransactionType type,
        LocalDateTime createdAt
) {
}
