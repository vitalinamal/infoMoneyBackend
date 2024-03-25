package prog.academy.infomoney.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prog.academy.infomoney.dto.request.WalletRequest;
import prog.academy.infomoney.dto.response.WalletResponse;
import prog.academy.infomoney.entity.Profile;
import prog.academy.infomoney.entity.Wallet;
import prog.academy.infomoney.exceptions.ApplicationException;
import prog.academy.infomoney.repository.TransactionRepository;
import prog.academy.infomoney.repository.WalletRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repository;
    private final ProfileService profileService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void createWallet(WalletRequest walletRequest) {
        Profile profile = profileService.getProfileById(walletRequest.profileId());

        profile.getWallets().stream().filter(wallet -> wallet.getName().equals(walletRequest.name()))
                .findAny().ifPresent(wallet -> {
                    throw new ApplicationException(STR."Wallet with this name (\{wallet.getName()}) already exists on this profile: \{profile.getName()}");
                });
        Wallet wallet = new Wallet();
        wallet.setName(walletRequest.name());
        wallet.setDescription(walletRequest.description());
        wallet.setProfile(profile);
        repository.save(wallet);
    }

    public List<WalletResponse> findAllWallets() {
        return repository.findAll().stream().map(this::mapToWalletResponse).collect(toList());
    }

    public List<WalletResponse> findAllWalletsByProfileId(Long profileId) {
        Profile profile = profileService.getProfileById(profileId);
        return repository.findAllByProfile(profile).stream().map(this::mapToWalletResponse).collect(toList());
    }

    public WalletResponse findWalletById(Long id) {
        var wallet = getWalletById(id);
        return mapToWalletResponse(wallet);
    }

    @Transactional
    public void updateWallet(Long id, WalletRequest request) {
        if (repository.existsByProfileIdAndNameAndNotId(request.name(), request.profileId(), id)) {
            throw new ApplicationException(STR."Profile already have a wallet with this name:  \{request.name()}");
        }

        Wallet wallet = getWalletById(id);
        wallet.setName(request.name());
        wallet.setDescription(request.description());
        repository.save(wallet);
    }

    @Transactional
    public void deleteWallet(Long id) {
        Wallet wallet = getWalletById(id);
        removeWalletFromTransactions(id, wallet);
        repository.delete(wallet);
    }

    private void removeWalletFromTransactions(Long id, Wallet wallet) {
        var wallets = transactionRepository.findAllByProfileAndWallet(wallet.getProfile(), id);
        wallets.forEach(t -> t.setWallet(null));
        transactionRepository.saveAll(wallets);
    }

    public Wallet getWalletById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApplicationException(STR."Wallet with id \{id} does not exist"));
    }

    private WalletResponse mapToWalletResponse(Wallet w) {
        return WalletResponse.builder()
                .id(w.getId())
                .name(w.getName())
                .description(w.getDescription())
                .build();
    }
}

