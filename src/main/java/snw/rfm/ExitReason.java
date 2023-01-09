package snw.rfm;

public enum ExitReason {

    BE_CAUGHT("被捕"),

    SELF_EXIT("已弃权");

    public final String MESSAGE;

    ExitReason(String message) {
        MESSAGE = message;
    }
}
