package prog.academy.infomoney.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prog.academy.infomoney.dto.request.TransactionRequest;
import prog.academy.infomoney.dto.response.ProfileTransactionsResponse;
import prog.academy.infomoney.dto.response.TotalTransactionsResponse;
import prog.academy.infomoney.service.TransactionService;

@RestController
@RequestMapping("/api/v1/protected/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @GetMapping
    public ResponseEntity<TotalTransactionsResponse> getTotalTransactionsForProfiles() {
        return ResponseEntity
                .ok(service.getTotalTransactionsForUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileTransactionsResponse> getTotalTransactionsForProfile(
            @PathVariable("id") Long profileId,
            @RequestParam(value = "walletId", required = false) Long walletId,
            @RequestParam(value = "categoryId", required = false) Long categoryId) {
        return ResponseEntity
                .ok(service.getTotalTransactionsForProfile(profileId, walletId, categoryId));
    }


    @PostMapping("/{id}")
    public ResponseEntity<Void> createTransaction(
            @PathVariable("id") Long profileId,
            @RequestBody TransactionRequest request) {
        service.createTransaction(request, profileId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{profileId}/update/{transactionId}")
    public ResponseEntity<Void> updateTransaction(
            @PathVariable("profileId") Long profileId,
            @PathVariable("transactionId") Long transactionId,
            @RequestBody TransactionRequest request) {
        service.updateTransaction(request, profileId, transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{profileId}/delete/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable("profileId") Long profileId,
            @PathVariable("transactionId") Long transactionId) {
        service.deleteTransaction(profileId, transactionId);
        return ResponseEntity
                .noContent()
                .build();
    }
}
