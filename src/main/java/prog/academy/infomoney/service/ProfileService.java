package prog.academy.infomoney.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prog.academy.infomoney.dto.request.ProfileCreateRequest;
import prog.academy.infomoney.dto.request.ProfileUpdateRequest;
import prog.academy.infomoney.entity.Profile;
import prog.academy.infomoney.entity.User;
import prog.academy.infomoney.exceptions.ApplicationException;
import prog.academy.infomoney.repository.ProfileRepository;

import java.security.Principal;

import static prog.academy.infomoney.utils.HelperUtils.checkIfProfileNameExist;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repository;

    @Transactional(readOnly = true)
    public Profile getProfileByName(String name, Long userId) {
        return getProfile(name, userId);
    }


    @Transactional
    public void createProfile(ProfileCreateRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (checkIfProfileNameExist(request.name(), user)) {
            throw new ApplicationException("Current user already have a profile with this name:  " + request.name());
        }

        repository.save(Profile.builder()
                .name(request.name())
                .user(user)
                .build());
    }

    @Transactional
    public void updateProfile(ProfileUpdateRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        var currentProfile = this.getProfile(request.currentName(), user.getId());

        if (checkIfProfileNameExist(request.newName(), user)) {
            throw new ApplicationException("Current user already have a profile with this name:  " + request.newName());
        }

        currentProfile.setName(request.newName());

        repository.save(currentProfile);
    }

    @Transactional
    public void deleteProfile(String name, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        var currentProfile = this.getProfile(name, user.getId());
        repository.deleteById(currentProfile.getId());
    }

    private Profile getProfile(String name, Long userId) {
        return repository
                .findByNameAndUserId(name, userId)
                .orElseThrow(() -> new ApplicationException("Current user doesn't have a profile with this name:  " + name));
    }
}
