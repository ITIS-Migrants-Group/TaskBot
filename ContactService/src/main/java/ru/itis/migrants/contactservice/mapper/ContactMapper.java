package ru.itis.migrants.contactservice.mapper;

import org.mapstruct.*;
import ru.itis.migrants.contactservice.dto.ContactResponse;
import ru.itis.migrants.contactservice.dto.UpdateContactRequest;
import ru.itis.migrants.contactservice.model.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    Contact toEntity(ContactResponse response);
    ContactResponse fromEntity(Contact entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "ownerId", ignore = true)
    void updateEntity(@MappingTarget Contact entity, UpdateContactRequest response);
}
