package com.lmora.saldapp.domain.exception;

public class GroupClosedException extends SaldAppException {
    public GroupClosedException() {
        super("Group is closed. No more transactions can be added.");
    }
}
