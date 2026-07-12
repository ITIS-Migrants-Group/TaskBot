package ru.itis.migrants.bot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.migrants.bot.models.enums.DialogState;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDialogData {

    private DialogState state = DialogState.IDLE;

    private String title;

    private String deadline;

    private String notifyPeriod;
}
