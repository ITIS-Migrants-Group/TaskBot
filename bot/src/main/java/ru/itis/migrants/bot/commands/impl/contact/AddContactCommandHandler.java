package ru.itis.migrants.bot.commands.impl.contact;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.migrants.bot.api.DefaultApi;
import ru.itis.migrants.bot.commands.DialogHandler;

import ru.itis.migrants.bot.model.Contact;
import ru.itis.migrants.bot.model.CreateContactRequest;
import ru.itis.migrants.bot.models.ContactDialogData;

import ru.itis.migrants.bot.models.enums.DialogState;
import ru.itis.migrants.bot.services.UserStateService;

@Component
@Slf4j
@RequiredArgsConstructor
public class AddContactCommandHandler implements DialogHandler {

    private final TelegramBot telegramBot;
    private final UserStateService userStateService;
    private final DefaultApi defaultApi;

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

        if (userStateService.isInDialog(chatId) && (!text.startsWith("/") || text.equals("/cancel"))) {
            return true;
        }

        return false;
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
            CreateContactRequest request = new CreateContactRequest();
            request.setName(data.getName());
            request.setPhoneNumber(data.getPhoneNumber());
            request.setEmail(data.getEmail());
            request.setCompany(data.getCompany());
            request.setNote(data.getNote());

            Contact contact = defaultApi.createContact(Math.toIntExact(chatId), request);

            StringBuilder response = new StringBuilder("✅ Контакт успешно создан!\n");
            response.append("Имя: ").append(contact.getName()).append("\n");
            if (contact.getPhoneNumber() != null)
                response.append("Телефон: ").append(contact.getPhoneNumber()).append("\n");
            if (contact.getEmail() != null)
                response.append("Email: ").append(contact.getEmail()).append("\n");
            if (contact.getCompany() != null)
                response.append("Компания: ").append(contact.getCompany()).append("\n");
            if (contact.getNote() != null)
                response.append("Заметка: ").append(contact.getNote());

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
