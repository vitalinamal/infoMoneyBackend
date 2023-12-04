package prog.academy.infomoney.enums;

import lombok.Getter;

@Getter
public enum TransactionType {
    INCOME("Income"),
    OUTCOME("Outcome");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

}
