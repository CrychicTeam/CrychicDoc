package dev.latvian.mods.kubejs.event;

import dev.architectury.event.CompoundEventResult;
import dev.latvian.mods.kubejs.util.UtilsJS;
import org.jetbrains.annotations.Nullable;

public class EventResult {

    public static final EventResult PASS = EventResult.Type.PASS.defaultResult;

    private final EventResult.Type type;

    private final Object value;

    private EventResult(EventResult.Type type, @Nullable Object value) {
        this.type = type;
        this.value = value;
    }

    public EventResult.Type type() {
        return this.type;
    }

    public Object value() {
        return this.value;
    }

    public boolean override() {
        return this.type != EventResult.Type.PASS;
    }

    public boolean pass() {
        return this.type == EventResult.Type.PASS;
    }

    public boolean error() {
        return this.type == EventResult.Type.ERROR;
    }

    public boolean interruptDefault() {
        return this.type == EventResult.Type.INTERRUPT_DEFAULT;
    }

    public boolean interruptFalse() {
        return this.type == EventResult.Type.INTERRUPT_FALSE;
    }

    public boolean interruptTrue() {
        return this.type == EventResult.Type.INTERRUPT_TRUE;
    }

    public dev.architectury.event.EventResult arch() {
        return this.type.defaultArchResult;
    }

    public <T> CompoundEventResult<T> archCompound() {
        return switch(this.type) {
            case INTERRUPT_DEFAULT ->
                CompoundEventResult.interruptDefault(UtilsJS.cast(this.value));
            case INTERRUPT_FALSE ->
                CompoundEventResult.interruptFalse(UtilsJS.cast(this.value));
            case INTERRUPT_TRUE ->
                CompoundEventResult.interruptTrue(UtilsJS.cast(this.value));
            default ->
                CompoundEventResult.pass();
        };
    }

    public static enum Type {

        ERROR(dev.architectury.event.EventResult.pass()), PASS(dev.architectury.event.EventResult.pass()), INTERRUPT_DEFAULT(dev.architectury.event.EventResult.interruptDefault()), INTERRUPT_FALSE(dev.architectury.event.EventResult.interruptFalse()), INTERRUPT_TRUE(dev.architectury.event.EventResult.interruptTrue());

        public final EventResult defaultResult = new EventResult(this, null);

        public final dev.architectury.event.EventResult defaultArchResult;

        public final EventExit defaultExit;

        private Type(dev.architectury.event.EventResult defaultArchResult) {
            this.defaultArchResult = defaultArchResult;
            this.defaultExit = new EventExit(this.defaultResult);
        }

        public EventExit exit(@Nullable Object value) {
            return value == null ? this.defaultExit : new EventExit(new EventResult(this, value));
        }
    }
}