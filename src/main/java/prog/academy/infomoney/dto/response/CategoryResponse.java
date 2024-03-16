package prog.academy.infomoney.dto.response;

import lombok.Builder;

@Builder
public record CategoryResponse(Long id, String name, String description) {
}
