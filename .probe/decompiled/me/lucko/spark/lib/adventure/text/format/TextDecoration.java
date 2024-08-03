package me.lucko.spark.lib.adventure.text.format;

import java.util.Objects;
import me.lucko.spark.lib.adventure.util.Index;
import me.lucko.spark.lib.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum TextDecoration implements StyleBuilderApplicable, TextFormat {

    OBFUSCATED("obfuscated"), BOLD("bold"), STRIKETHROUGH("strikethrough"), UNDERLINED("underlined"), ITALIC("italic");

    public static final Index<String, TextDecoration> NAMES = Index.create(TextDecoration.class, constant -> constant.name);

    private final String name;

    private TextDecoration(final String name) {
        this.name = name;
    }

    @Deprecated
    @NotNull
    public final TextDecorationAndState as(final boolean state) {
        return this.withState(state);
    }

    @Deprecated
    @NotNull
    public final TextDecorationAndState as(@NotNull final TextDecoration.State state) {
        return this.withState(state);
    }

    @NotNull
    public final TextDecorationAndState withState(final boolean state) {
        return new TextDecorationAndStateImpl(this, TextDecoration.State.byBoolean(state));
    }

    @NotNull
    public final TextDecorationAndState withState(@NotNull final TextDecoration.State state) {
        return new TextDecorationAndStateImpl(this, state);
    }

    @NotNull
    public final TextDecorationAndState withState(@NotNull final TriState state) {
        return new TextDecorationAndStateImpl(this, TextDecoration.State.byTriState(state));
    }

    @Override
    public void styleApply(@NotNull final Style.Builder style) {
        style.decorate(this);
    }

    @NotNull
    public String toString() {
        return this.name;
    }

    public static enum State {

        NOT_SET("not_set"), FALSE("false"), TRUE("true");

        private final String name;

        private State(final String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        @NotNull
        public static TextDecoration.State byBoolean(final boolean flag) {
            return flag ? TRUE : FALSE;
        }

        @NotNull
        public static TextDecoration.State byBoolean(@Nullable final Boolean flag) {
            return flag == null ? NOT_SET : byBoolean(flag.booleanValue());
        }

        @NotNull
        public static TextDecoration.State byTriState(@NotNull final TriState flag) {
            Objects.requireNonNull(flag);
            switch(flag) {
                case TRUE:
                    return TRUE;
                case FALSE:
                    return FALSE;
                case NOT_SET:
                    return NOT_SET;
                default:
                    throw new IllegalArgumentException("Unable to turn TriState: " + flag + " into a TextDecoration.State");
            }
        }
    }
}