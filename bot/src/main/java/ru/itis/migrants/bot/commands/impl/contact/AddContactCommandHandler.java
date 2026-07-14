package ru.itis.migrants.bot.commands.impl.contact;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.itis.migrants.bot.client.GatewayClient;
import ru.itis.migrants.bot.commands.DialogHandler;

import ru.itis.migrants.bot.dto.request.CreateContactRequest;
import ru.itis.migrants.bot.dto.response.ContactResponse;
import ru.itis.migrants.bot.models.ContactDialogData;

import ru.itis.migrants.bot.models.enums.DialogState;
import ru.itis.migrants.bot.services.UserStateService;

@Component
@Slf4j
@RequiredArgsConstructor
public class AddContactCommandHandler implements DialogHandler {

    private final TelegramBot telegramBot;
    private final UserStateService userStateService;
    private final GatewayClient defaultApi;

    @Override
    public boolean supports(Update update) {
        Message message = update.message();
        if (message == null) return false;
        String text = message.text();
        if (text == null) return false;
        Long chatId = message.chat().id();

        if (text.equals("/addcontact")) {
            return true;
        }

        if (text.equals("/cancel")) {
            return userStateService.isInDialog(chatId);
        }

        DialogState state = userStateService.getState(chatId);
        return state == DialogState.AWAITING_CONTACT_NAME ||
                state == DialogState.AWAITING_PHONE ||
                state == DialogState.AWAITING_COMPANY ||
                state == DialogState.AWAITING_EMAIL ||
                state == DialogState.AWAITING_NOTE;
    }

    @Override
    public void handle(Update update) {
        Message message = update.message();
        Long chatId = message.chat().id();
        String text = message.text();

        if (text.equals("/addcontact")) {
            userStateService.clearAll(chatId);
            userStateService.setState(chatId, DialogState.AWAITING_CONTACT_NAME);
            sendMessage(chatId, "Введите имя контакта (обязательно):");
            return;
        }

        if (text.equals("/cancel")) {
            userStateService.clearAll(chatId);
            sendMessage(chatId, "Создание контакта отменено.");
            return;
        }

        DialogState currentState = userStateService.getState(chatId);
        ContactDialogData data = userStateService.getContactDialog(chatId);

        switch (currentState) {
            case AWAITING_CONTACT_NAME:
                if (text.trim().isEmpty()) {
                    sendMessage(chatId, "Имя не может быть пустым. Введите имя:");
                    return;
                }
                data.setName(text.trim());
                userStateService.setState(chatId, DialogState.AWAITING_PHONE);
                sendMessage(chatId, "Введите номер телефона (или /skip):");
                break;

            case AWAITING_PHONE:
                if (!text.equals("/skip")) {
                    data.setPhoneNumber(text.trim());
                }
                userStateService.setState(chatId, DialogState.AWAITING_EMAIL);
                sendMessage(chatId, "Введите email (или /skip):");
                break;

            case AWAITING_EMAIL:
                if (!text.equals("/skip")) {
                    data.setEmail(text.trim());
                }
                userStateService.setState(chatId, DialogState.AWAITING_COMPANY);
                sendMessage(chatId, "Введите компанию (или /skip):");
                break;

            case AWAITING_COMPANY:
                if (!text.equals("/skip")) {
                    data.setCompany(text.trim());
                }
                userStateService.setState(chatId, DialogState.AWAITING_NOTE);
                sendMessage(chatId, "Введите заметку (или /skip):");
                break;

            case AWAITING_NOTE:
                if (!text.equals("/skip")) {
                    data.setNote(text.trim());
                }
                createContact(chatId, data);
                break;

            default:
                sendMessage(chatId, "Произошла ошибка. Попробуйте начать заново с /addcontact");
                userStateService.clearAll(chatId);
        }
    }

    private void createContact(Long chatId, ContactDialogData data) {
        try {
            CreateContactRequest request = new CreateContactRequest(
                    data.getName(),
                    data.getPhoneNumber(),
                    data.getEmail(),
                    data.getCompany(),
                    data.getNote()
            );
            log.debug("Создание контакта: {}", request);
            ContactResponse contact = defaultApi.createContact(chatId, request).getBody();

            StringBuilder response = new StringBuilder("✅ Контакт успешно создан!\n");
            response.append("Имя: ").append(contact.name()).append("\n");
            if (contact.phoneNumber() != null)
                response.append("Телефон: ").append(contact.phoneNumber()).append("\n");
            if (contact.email() != null)
                response.append("Email: ").append(contact.email()).append("\n");
            if (contact.company() != null)
                response.append("Компания: ").append(contact.company()).append("\n");
            if (contact.note() != null)
                response.append("Заметка: ").append(contact.note());

            sendMessage(chatId, response.toString());

            userStateService.clearAll(chatId);

        } catch (Exception e) {
            log.error("Ошибка при создании контакта для чата {}", chatId, e);
            sendMessage(chatId, "❌ Не удалось создать контакт. Попробуйте позже.");
            userStateService.clearAll(chatId);
        }
    }

    private void sendMessage(Long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }
}
