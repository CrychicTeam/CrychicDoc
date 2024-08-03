package icyllis.modernui.core;

import icyllis.modernui.annotation.Nullable;
import java.util.Objects;

public class OperationCanceledException extends RuntimeException {

    public OperationCanceledException() {
        this(null);
    }

    public OperationCanceledException(@Nullable String message) {
        super(Objects.toString(message, "The operation has been canceled."));
    }
}