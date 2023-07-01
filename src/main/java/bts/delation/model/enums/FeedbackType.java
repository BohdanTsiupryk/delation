package bts.delation.model.enums;

import java.util.Arrays;

public enum FeedbackType {

    NONE("none", "ПУСТО"),
    ANOTHER("another", "ІНШЕ"),
    FEEDBACK("feedback", "ВІДГУК"),
    APPEAL("appeal", "СКАРГА"),
    APPEAL_MODER("appeal_moder", "СКАРГА НА МОДЕРА"),
    OFFER("offer", "ПРОПОЗИЦІЯ"),
    BUG("bug", "БАГ");

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
