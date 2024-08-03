package me.lucko.spark.lib.adventure.text.event;

import java.net.URL;
import java.util.Objects;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.text.format.Style;
import me.lucko.spark.lib.adventure.text.format.StyleBuilderApplicable;
import me.lucko.spark.lib.adventure.util.Index;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ClickEvent implements Examinable, StyleBuilderApplicable {

    private final ClickEvent.Action action;

    private final String value;

    @NotNull
    public static ClickEvent openUrl(@NotNull final String url) {
        return new ClickEvent(ClickEvent.Action.OPEN_URL, url);
    }

    @NotNull
    public static ClickEvent openUrl(@NotNull final URL url) {
        return openUrl(url.toExternalForm());
    }

    @NotNull
    public static ClickEvent openFile(@NotNull final String file) {
        return new ClickEvent(ClickEvent.Action.OPEN_FILE, file);
    }

    @NotNull
    public static ClickEvent runCommand(@NotNull final String command) {
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
    }

    @NotNull
    public static ClickEvent suggestCommand(@NotNull final String command) {
        return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
    }

    @NotNull
    public static ClickEvent changePage(@NotNull final String page) {
        return new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page);
    }

    @NotNull
    public static ClickEvent changePage(final int page) {
        return changePage(String.valueOf(page));
    }

    @NotNull
    public static ClickEvent copyToClipboard(@NotNull final String text) {
        return new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, text);
    }

    @NotNull
    public static ClickEvent clickEvent(@NotNull final ClickEvent.Action action, @NotNull final String value) {
        return new ClickEvent(action, value);
    }

    private ClickEvent(@NotNull final ClickEvent.Action action, @NotNull final String value) {
        this.action = (ClickEvent.Action) Objects.requireNonNull(action, "action");
        this.value = (String) Objects.requireNonNull(value, "value");
    }

    @NotNull
    public ClickEvent.Action action() {
        return this.action;
    }

    @NotNull
    public String value() {
        return this.value;
    }

    @Override
    public void styleApply(@NotNull final Style.Builder style) {
        style.clickEvent(this);
    }

    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            ClickEvent that = (ClickEvent) other;
            return this.action == that.action && Objects.equals(this.value, that.value);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.action.hashCode();
        return 31 * result + this.value.hashCode();
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("action", this.action), ExaminableProperty.of("value", this.value));
    }

    public String toString() {
        return Internals.toString(this);
    }

    public static enum Action {

        OPEN_URL("open_url", true),
        OPEN_FILE("open_file", false),
        RUN_COMMAND("run_command", true),
        SUGGEST_COMMAND("suggest_command", true),
        CHANGE_PAGE("change_page", true),
        COPY_TO_CLIPBOARD("copy_to_clipboard", true);

        public static final Index<String, ClickEvent.Action> NAMES = Index.create(ClickEvent.Action.class, constant -> constant.name);

        private final String name;

        private final boolean readable;

        private Action(@NotNull final String name, final boolean readable) {
            this.name = name;
            this.readable = readable;
        }

        public boolean readable() {
            return this.readable;
        }

        @NotNull
        public String toString() {
            return this.name;
        }
    }
}