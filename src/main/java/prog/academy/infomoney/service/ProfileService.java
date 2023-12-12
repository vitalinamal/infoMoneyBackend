package prog.academy.infomoney.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prog.academy.infomoney.dto.request.ProfileCreateRequest;
import prog.academy.infomoney.dto.request.ProfileUpdateRequest;
import prog.academy.infomoney.entity.Profile;
import prog.academy.infomoney.exceptions.ApplicationException;
import prog.academy.infomoney.repository.ProfileRepository;

import java.util.List;

import static prog.academy.infomoney.utils.HelperUtils.checkIfProfileNameExist;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repository;

    @Transactional(readOnly = true)
    public Profile getProfileByName(String name) {
        return getProfile(name);
    }


    @Transactional(readOnly = true)
    public List<Profile> getProfiles() {
        return repository.findAll();
    }


    @Transactional
    public void createProfile(ProfileCreateRequest request) {
        var profiles = this.repository.findAll();

        if (checkIfProfileNameExist(request.name(), profiles)) {
            throw new ApplicationException("Current user already have a profile with this name:  " + request.name());
        }

        repository.save(Profile.builder()
                .name(request.name())
                .build());
    }

    @Transactional
    public void updateProfile(ProfileUpdateRequest request) {

        var profiles = this.repository.findAll();

        var currentProfile = this.getProfile(request.currentName());

        if (checkIfProfileNameExist(request.newName(), profiles)) {
            throw new ApplicationException("Current user already have a profile with this name:  " + request.newName());
        }

        currentProfile.setName(request.newName());

        repository.save(currentProfile);
    }

    @Transactional
    public void deleteProfile(String name) {
        var currentProfile = this.getProfile(name);
        repository.deleteById(currentProfile.getId());
    }

    private Profile getProfile(String name) {
        return repository
                .findByName(name)
                .orElseThrow(() -> new ApplicationException("Current user doesn't have a profile with this name:  " + name));
    }
}
