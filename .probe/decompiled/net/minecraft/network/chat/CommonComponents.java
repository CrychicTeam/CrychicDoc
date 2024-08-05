package net.minecraft.network.chat;

import java.util.Arrays;
import java.util.Collection;

public class CommonComponents {

    public static final Component EMPTY = Component.empty();

    public static final Component OPTION_ON = Component.translatable("options.on");

    public static final Component OPTION_OFF = Component.translatable("options.off");

    public static final Component GUI_DONE = Component.translatable("gui.done");

    public static final Component GUI_CANCEL = Component.translatable("gui.cancel");

    public static final Component GUI_YES = Component.translatable("gui.yes");

    public static final Component GUI_NO = Component.translatable("gui.no");

    public static final Component GUI_OK = Component.translatable("gui.ok");

    public static final Component GUI_PROCEED = Component.translatable("gui.proceed");

    public static final Component GUI_CONTINUE = Component.translatable("gui.continue");

    public static final Component GUI_BACK = Component.translatable("gui.back");

    public static final Component GUI_TO_TITLE = Component.translatable("gui.toTitle");

    public static final Component GUI_ACKNOWLEDGE = Component.translatable("gui.acknowledge");

    public static final Component GUI_OPEN_IN_BROWSER = Component.translatable("chat.link.open");

    public static final Component GUI_COPY_LINK_TO_CLIPBOARD = Component.translatable("gui.copy_link_to_clipboard");

    public static final Component CONNECT_FAILED = Component.translatable("connect.failed");

    public static final Component NEW_LINE = Component.literal("\n");

    public static final Component NARRATION_SEPARATOR = Component.literal(". ");

    public static final Component ELLIPSIS = Component.literal("...");

    public static final Component SPACE = space();

    public static MutableComponent space() {
        return Component.literal(" ");
    }

    public static MutableComponent days(long long0) {
        return Component.translatable("gui.days", long0);
    }

    public static MutableComponent hours(long long0) {
        return Component.translatable("gui.hours", long0);
    }

    public static MutableComponent minutes(long long0) {
        return Component.translatable("gui.minutes", long0);
    }

    public static Component optionStatus(boolean boolean0) {
        return boolean0 ? OPTION_ON : OPTION_OFF;
    }

    public static MutableComponent optionStatus(Component component0, boolean boolean1) {
        return Component.translatable(boolean1 ? "options.on.composed" : "options.off.composed", component0);
    }

    public static MutableComponent optionNameValue(Component component0, Component component1) {
        return Component.translatable("options.generic_value", component0, component1);
    }

    public static MutableComponent joinForNarration(Component... component0) {
        MutableComponent $$1 = Component.empty();
        for (int $$2 = 0; $$2 < component0.length; $$2++) {
            $$1.append(component0[$$2]);
            if ($$2 != component0.length - 1) {
                $$1.append(NARRATION_SEPARATOR);
            }
        }
        return $$1;
    }

    public static Component joinLines(Component... component0) {
        return joinLines(Arrays.asList(component0));
    }

    public static Component joinLines(Collection<? extends Component> collectionExtendsComponent0) {
        return ComponentUtils.formatList(collectionExtendsComponent0, NEW_LINE);
    }
}