package ru.itis.migrants.contactservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.migrants.contactservice.dto.ContactResponse;
import ru.itis.migrants.contactservice.dto.CreateContactRequest;
import ru.itis.migrants.contactservice.dto.UpdateContactRequest;
import ru.itis.migrants.contactservice.service.ContactService;
import ru.itis.migrants.starter.logging.annotation.LogAround;

import java.util.List;
import java.util.UUID;

@LogAround
@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;
    @GetMapping("/{tg-id}")
    public ResponseEntity<List<ContactResponse>> getContacts(@PathVariable("tg-id") Long tgId) {
        List<ContactResponse> response = contactService.getContacts(tgId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{tg-id}/search")
    public ResponseEntity<List<ContactResponse>> searchContacts(@PathVariable("tg-id") Long tgId,
                                                                @RequestParam("query") @NotBlank String query) {
        List<ContactResponse> response = contactService.searchContacts(tgId, query);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{tg-id}")
    public ResponseEntity<ContactResponse> createContact(@PathVariable("tg-id") Long tgId,
                                                         @RequestBody @Valid CreateContactRequest request) {
        ContactResponse response = contactService.createContact(tgId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{tg-id}")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable("tg-id") Long tgId,
                                                         @RequestBody @Valid UpdateContactRequest request) {
        ContactResponse response = contactService.updateContact(tgId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{tg-id}/{id}")
    public ResponseEntity<ContactResponse> updateContact(@PathVariable("tg-id") Long tgId,
                                                            @PathVariable UUID id) {
        ContactResponse response = contactService.deleteContact(tgId, id);
        return ResponseEntity.ok(response);
    }
}
