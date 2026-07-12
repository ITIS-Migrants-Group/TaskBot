package ru.itis.migrants.bot.models;

import lombok.Data;

@Data
public class ContactDialogData {
    private String name;

    private String phoneNumber;

    private String email;

    private String company;

    private String note;
}