package com.mna.network.messages;

public abstract class BaseMessage {

    protected boolean messageIsValid = false;

    public final boolean isMessageValid() {
        return this.messageIsValid;
    }
}