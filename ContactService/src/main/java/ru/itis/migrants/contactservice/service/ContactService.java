package ru.itis.migrants.contactservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.migrants.contactservice.dto.ContactResponse;
import ru.itis.migrants.contactservice.dto.CreateContactRequest;
import ru.itis.migrants.contactservice.dto.UpdateContactRequest;
import ru.itis.migrants.contactservice.mapper.ContactMapper;
import ru.itis.migrants.contactservice.model.Contact;
import ru.itis.migrants.contactservice.repository.ContactJpaRepository;
import ru.itis.migrants.starter.logging.annotation.LogAround;

import java.util.List;
import java.util.UUID;

@LogAround
@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactJpaRepository contactJpaRepository;
    private final ContactMapper contactMapper;

    public List<ContactResponse> getContacts(Long ownerId) {
        if (ownerId == null)  {
            throw new IllegalArgumentException("ownerId must be not null");
        }

        List<Contact> contacts = contactJpaRepository.findContactByOwnerId(ownerId);
        return contacts.stream().map(contactMapper::fromEntity).toList();
    }

    public List<ContactResponse> searchContacts(Long ownerId, String query) {
        if (ownerId == null || query == null || query.isBlank()) {
            throw new IllegalArgumentException("incorrect ownerId or query");
        }

        List<Contact> contacts = contactJpaRepository.searchByOwnerIdAndQuery(ownerId, query);
        return contacts.stream().map(contactMapper::fromEntity).toList();
    }

    @Transactional
    public ContactResponse createContact(Long tgId, CreateContactRequest request) {
        Contact contact = Contact.builder()
                .ownerId(tgId)
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .company(request.company())
                .note(request.note())
                .build();

        Contact saved = contactJpaRepository.save(contact);
        return contactMapper.fromEntity(saved);
    }

    @Transactional
    public ContactResponse updateContact(Long tgId, UpdateContactRequest request) {
        if (tgId == null) {
            throw new IllegalArgumentException("tgId dont matches");
        }
        Contact contact = contactJpaRepository.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("contact not found"));

        if (!contact.getOwnerId().equals(tgId)) {
            throw new IllegalStateException("tgId and owner dont matches");
        }

        contactMapper.updateEntity(contact, request);
        Contact saved = contactJpaRepository.save(contact);
        return contactMapper.fromEntity(saved);
    }

    @Transactional
    public ContactResponse deleteContact(Long tgId, UUID id) {
        if (tgId == null || id == null) {
            throw new IllegalArgumentException("tgId or id dont matches");
        }
        Contact contact = contactJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("contact not found"));

        if (!contact.getOwnerId().equals(tgId)) {
            throw new IllegalStateException("tgId and owner dont matches");
        }

        contactJpaRepository.delete(contact);
        return contactMapper.fromEntity(contact);
    }
}
