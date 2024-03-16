package prog.academy.infomoney.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prog.academy.infomoney.dto.request.WalletRequest;
import prog.academy.infomoney.dto.response.WalletResponse;
import prog.academy.infomoney.entity.Wallet;
import prog.academy.infomoney.service.WalletService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/protected/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody WalletRequest walletRequest) {
        walletService.createWallet(walletRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<WalletResponse>> getAllWalletsByProfile(@PathVariable Long profileId) {
        return ResponseEntity.ok(walletService.findAllWalletsByProfileId(profileId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<WalletResponse> getWalletById(@PathVariable Long id) {
        return ResponseEntity.ok(walletService.findWalletById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wallet> updateWallet(@PathVariable Long id, @RequestBody WalletRequest walletRequest) {
        walletService.updateWallet(id, walletRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet(id);
        return ResponseEntity.noContent().build();
    }
}
