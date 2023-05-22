package bts.delation.model;

import java.util.Arrays;

public enum FeedbackType {

    NONE("none", "пусто"),
    ANOTHER("another", "інше"),
    FEEDBACK("feedback", "відгук"),
    APPEAL("appeal", "скарга"),
    APPEAL_MODER("appeal_moder", "скарга на модера"),
    BUG("bug", "помилка");

    private final String value;
    private final String ua;

    FeedbackType(String value, String ua) {
        this.value = value;
        this.ua = ua;
    }

    public String getValue() {
        return value;
    }

    public String getUa() {
        return ua;
    }

    public static FeedbackType getTypeByValue(String value) {
        return Arrays.stream(FeedbackType.values())
                .filter(f -> f.value.equals(value))
                .findFirst()
                .orElse(NONE);
    }
}
