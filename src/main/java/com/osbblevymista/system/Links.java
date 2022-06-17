package com.osbblevymista.system;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Links {
    OSBB_TELEGRAM_CHAT_TEREVENI("https://t.me/+0F5deHu6kgljYmY6"),
    OSBB_TELEGRAM_CHAT_ANNOUNCEMENT("https://t.me/+Y2SsNGZUwaw3OTUy"),
    OSBB_TELEGRAM_CHAT_BOARD("https://t.me/+x_BfDxLsXxk3Nzdi"),
    CAR_ENTRANCE("https://docs.google.com/forms/d/1IiGX38cfMiF3A6JntIzOtHeQejoUvXp8QFI26ybfk0M/viewform?edit_requested=true"),
    RULES_OF_LEAVING("https://bit.ly/OSBBRulsOfLeaving"),
    TARIFFS("https://bit.ly/OSBBTariffs"),
    MEETING_RESULT01022022("https://bit.ly/OSBBMeetingResult01022022");

    private String link;
}
