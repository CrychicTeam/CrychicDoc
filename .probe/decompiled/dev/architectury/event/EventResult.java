package dev.architectury.event;

import net.minecraft.world.InteractionResult;
import org.apache.commons.lang3.BooleanUtils;

public final class EventResult {

    private static final EventResult TRUE = new EventResult(true, true);

    private static final EventResult STOP = new EventResult(true, null);

    private static final EventResult PASS = new EventResult(false, null);

    private static final EventResult FALSE = new EventResult(true, false);

    private final boolean interruptsFurtherEvaluation;

    private final Boolean value;

    public static EventResult pass() {
        return PASS;
    }

    public static EventResult interrupt(Boolean value) {
        if (value == null) {
            return STOP;
        } else {
            return value ? TRUE : FALSE;
        }
    }

    public static EventResult interruptTrue() {
        return TRUE;
    }

    public static EventResult interruptDefault() {
        return STOP;
    }

    public static EventResult interruptFalse() {
        return FALSE;
    }

    EventResult(boolean interruptsFurtherEvaluation, Boolean value) {
        this.interruptsFurtherEvaluation = interruptsFurtherEvaluation;
        this.value = value;
    }

    public boolean interruptsFurtherEvaluation() {
        return this.interruptsFurtherEvaluation;
    }

    public Boolean value() {
        return this.value;
    }

    public boolean isEmpty() {
        return this.value == null;
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public boolean isTrue() {
        return BooleanUtils.isTrue(this.value);
    }

    public boolean isFalse() {
        return BooleanUtils.isFalse(this.value);
    }

    public InteractionResult asMinecraft() {
        if (this.isPresent()) {
            return this.value() ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        } else {
            return InteractionResult.PASS;
        }
    }
}