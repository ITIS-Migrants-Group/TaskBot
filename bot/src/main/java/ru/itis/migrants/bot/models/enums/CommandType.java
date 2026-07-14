package ru.itis.migrants.bot.models.enums;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum CommandType {
    START(
            "/start",
            "Регистрация пользователя",
            "🚀 Начать",
            0
    ),

    TASKS(
            "/tasks",
            "Получение задач",
            "📋 Мои задачи",
            0
    ),

    ADDTASK(
            "/addtask",
            "Создание задачи",
            "➕ Добавить задачу",
            1
    ),

    ADDNOTIFICATION(
            "/addnotification",
            "Создание уведомления",
            "🔔 Уведомление",
            1
    ),

    ADDCONTACT(
            "/addcontact",
            "Добавить контакт",
            "👤 Контакт",
            2
    ),

    ADDDOCUMENT(
            "/adddocument",
            "Добавить документ",
            "📄 Документ",
            2
    ),

    SEARCH(
            "/search",
            "Поиск",
            "🔎 Поиск",
            3
    ),

    SEARCHCONTACTS(
            "/searchcontacts",
            "Поиск контактов",
            "👥 Контакты",
            3
    ),

    MENU(
            "/menu",
            "Показать меню команд в виде кнопок",
            "МЕНЮ КНОПОК",
            1
    ),

    UNKNOWN(
            "",
            "Неизвестная команда",
            "",
            -1,
            false
    );


    private final String type;
    private final String description;
    private final String buttonText;
    private final int keyboardRow;
    private final boolean enabled;

    CommandType(
            String type,
            String description,
            String buttonText,
            int keyboardRow
    ) {
        this(type, description, buttonText, keyboardRow, true);
    }


    CommandType(
            String type,
            String description,
            String buttonText,
            int keyboardRow,
            boolean enabled
    ) {
        this.type = type;
        this.description = description;
        this.buttonText = buttonText;
        this.keyboardRow = keyboardRow;
        this.enabled = enabled;
    }

    public String getCommandTypeWithoutSlash() {
        return type.startsWith("/")
                ? type.substring(1)
                : type;
    }
    public static CommandType getCommandTypeFromString(String text) {
        if (text == null) {
            return UNKNOWN;
        }
        String commandPart = text.toLowerCase().split(" ")[0];
        for (CommandType type : values()) {
            if (type.type.equals(commandPart)
                    || type.buttonText.equals(text)) {
                return type;
            }
        }
        return UNKNOWN;
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
