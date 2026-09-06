package br.com.pedro.core.usecases;

import java.util.List;

public class UseCaseError {

    private final UseCaseErrorType type;
    private final List<String> messages;

    public UseCaseError(UseCaseErrorType type, List<String> messages) {
        this.type = type;
        this.messages = messages;
    }

    public UseCaseErrorType getType() {
        return type;
    }

    public List<String> getMessages() {
        return messages;
    }
}
