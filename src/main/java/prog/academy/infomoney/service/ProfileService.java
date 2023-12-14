package prog.academy.infomoney.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import prog.academy.infomoney.dto.request.ProfileRequest;
import prog.academy.infomoney.entity.Profile;
import prog.academy.infomoney.exceptions.ApplicationException;
import prog.academy.infomoney.repository.ProfileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repository;

    @Transactional(readOnly = true)
    public Profile getProfileByName(Long profileId) {
        return getProfile(profileId);
    }


    @Transactional(readOnly = true)
    public List<Profile> getProfiles() {
        return repository.findAll();
    }


    @Transactional
    public void createProfile(ProfileRequest request) {
        if (repository.existsByName(request.name())) {
            throw new ApplicationException("Current user already have a profile with this name:  " + request.name());
        }

        repository.save(Profile.builder()
                .name(request.name())
                .build());
    }

    @Transactional
    public void updateProfile(Long id, ProfileRequest request) {

        var currentProfile = this.getProfile(id);

        if (repository.existsByName(request.name())) {
            throw new ApplicationException("Current user already have a profile with this name:  " + request.name());
        }

        currentProfile.setName(request.name());

        repository.save(currentProfile);
    }

    @Transactional
    public void deleteProfile(Long id) {
        var currentProfile = this.getProfile(id);
        repository.deleteById(currentProfile.getId());
    }

    private Profile getProfile(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ApplicationException("Current user doesn't have a profile with this id:  " + id));
    }
}
