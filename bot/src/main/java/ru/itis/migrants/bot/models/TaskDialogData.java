package ru.itis.migrants.bot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.migrants.bot.models.enums.DialogState;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDialogData {

    private DialogState state = DialogState.IDLE;

    private String title;

    private OffsetDateTime deadline;

    private String notifyPeriod;
}
