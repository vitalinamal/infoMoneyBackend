package prog.academy.infomoney.dto.request;

public record ChangePasswordRequest(String currentPassword, String newPassword, String confirmationPassword) {
}
