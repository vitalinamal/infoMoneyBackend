package prog.academy.infomoney.dto.request;

import java.math.BigDecimal;

public record TransactionRequest(BigDecimal amount,
                                 String description,
                                 String type,
                                 Long categoryId,
                                 Long walletId,
                                 String createdAt) {
}
