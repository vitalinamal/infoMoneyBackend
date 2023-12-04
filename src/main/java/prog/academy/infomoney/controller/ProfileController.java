package prog.academy.infomoney.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prog.academy.infomoney.dto.request.ProfileCreateRequest;
import prog.academy.infomoney.dto.request.ProfileUpdateRequest;
import prog.academy.infomoney.service.ProfileService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/protected/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    @PostMapping
    public ResponseEntity<Void> createProfile(@RequestBody ProfileCreateRequest request, Principal connectedUser) {
        service.createProfile(request, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequest request, Principal connectedUser) {
        service.updateProfile(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> updateProfile(@PathVariable("name") String name, Principal connectedUser) {
        service.deleteProfile(name, connectedUser);
        return ResponseEntity.noContent().build();
    }
}
