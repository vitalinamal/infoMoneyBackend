package prog.academy.infomoney.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prog.academy.infomoney.dto.request.TransactionRequest;
import prog.academy.infomoney.dto.response.UserProfileTransactionsResponse;
import prog.academy.infomoney.dto.response.UserTotalTransactionsResponse;
import prog.academy.infomoney.service.TransactionService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/protected/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping
    public ResponseEntity<UserTotalTransactionsResponse> getTotalTransactionsForUsers(Principal connectedUser) {
        return ResponseEntity.ok(service.getTotalTransactionsForUsers(connectedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileTransactionsResponse> getTotalTransactionsForUserProfile(@PathVariable("id") String profileName,
                                                                                              Principal connectedUser) {
        return ResponseEntity.ok(service.getTotalTransactionsForUserProfile(profileName, connectedUser));
    }


    @PostMapping("/{id}")
    public ResponseEntity<Void> createTransaction(
            @PathVariable("id") String profileName,
            @RequestBody TransactionRequest request, Principal connectedUser) {
        service.createTransaction(request, profileName, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
