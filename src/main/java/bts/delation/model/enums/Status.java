package bts.delation.model.enums;

public enum Status {
    NEW(1), IN_PROGRESS(2), VALIDATION(3), DONE(4), CANCELED(5);

    private final int priority;
    Status(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }
}
