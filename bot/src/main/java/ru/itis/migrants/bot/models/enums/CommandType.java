package ru.itis.migrants.bot.models.enums;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum CommandType {
    START("/start", "Регистрация пользователя"),
    TASKS("/tasks", "Получение задач(с и без фильтров)"),
    ADDTASK("/addtask", "Создание новой задачи"),
    COMPLETETASK("/completetask", "Завершить задачу"),
    CONTACTS("/contacts", "Просмотр контактов"),
    ADDCONTACT("/addcontact", "Добавить контакт"),
    EDITCONTACT("/editcontact", "Редактировать контакт"),
    DELETECONTACT("/deletecontact", "Удалить контакт"),
    SEARCHCONTACTS("/searchcontacts", "Поиск по контактам"),
    DOCUMENTS("/documents", "Поиск документов"),
    ADDDOCUMENT("/adddocument", "Добавить документ"),
    DELETEDOCUMENT("/deletedocument", "Удалить документ"),
    NOTIFICATIONS("/notifications", "Просмотр уведомлений"),
    ADDNOTIFICATION("/addnotification", "Создать уведомление"),
    DELETENOTIFICATION("/deletenotification", "Удалить уведомление"),
    SEARCH("/search", "Глобальный поиск по всем данным"),
    UNKNOWN("", "Неизвестная команда", false);

    private final String type;
    private final String description;
    private final boolean enabled;

    CommandType(String type, String description) {
        this(type, description, true);
    }

    CommandType(String type, String description, boolean enabled) {
        this.type = type;
        this.description = description;
        this.enabled = enabled;
    }

    public static CommandType getCommandTypeFromString(String text) {
        for (CommandType type : values()) {
            String commandPart = text.toLowerCase().split(" ")[0];
            if (type.type.equals(commandPart)) {
                return type;
            }
        }

        return UNKNOWN;
    }

    public String getCommandTypeWithoutSlash() {
        if (type.startsWith("/")) {
            return type.substring(1).toLowerCase();
        } else {
            return type.toLowerCase();
        }
    }

    public static String getAllCommandsForCommandHelp() {
        StringBuilder list = new StringBuilder();
        for (CommandType type : values()) {
            if (type.enabled) {
                list.append(type.type).append(" - ").append(type.description).append("\n");
            }
        }
        return list.toString();
    }
}
