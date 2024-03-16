package prog.academy.infomoney.dto.response;

import lombok.Builder;

@Builder
public record WalletResponse(Long id, String name, String description) {
}
