package prog.academy.infomoney.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prog.academy.infomoney.dto.request.ProfileRequest;
import prog.academy.infomoney.service.ProfileService;

@RestController
@RequestMapping("/api/v1/protected/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    @PostMapping
    public ResponseEntity<Void> createProfile(@RequestBody ProfileRequest request) {
        service.createProfile(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProfile(@PathVariable("id") Long id, @RequestBody ProfileRequest request) {
        service.updateProfile(id, request);
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable("id") Long id) {
        service.deleteProfile(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
