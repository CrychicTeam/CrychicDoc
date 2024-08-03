package net.minecraft.client.gui.screens;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ConfirmLinkScreen extends ConfirmScreen {

    private static final Component COPY_BUTTON_TEXT = Component.translatable("chat.copy");

    private static final Component WARNING_TEXT = Component.translatable("chat.link.warning");

    private final String url;

    private final boolean showWarning;

    public ConfirmLinkScreen(BooleanConsumer booleanConsumer0, String string1, boolean boolean2) {
        this(booleanConsumer0, confirmMessage(boolean2), Component.literal(string1), string1, boolean2 ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_NO, boolean2);
    }

    public ConfirmLinkScreen(BooleanConsumer booleanConsumer0, Component component1, String string2, boolean boolean3) {
        this(booleanConsumer0, component1, string2, boolean3 ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_NO, boolean3);
    }

    public ConfirmLinkScreen(BooleanConsumer booleanConsumer0, Component component1, String string2, Component component3, boolean boolean4) {
        this(booleanConsumer0, component1, confirmMessage(boolean4, string2), string2, component3, boolean4);
    }

    public ConfirmLinkScreen(BooleanConsumer booleanConsumer0, Component component1, Component component2, String string3, Component component4, boolean boolean5) {
        super(booleanConsumer0, component1, component2);
        this.f_95647_ = (Component) (boolean5 ? Component.translatable("chat.link.open") : CommonComponents.GUI_YES);
        this.f_95648_ = component4;
        this.showWarning = !boolean5;
        this.url = string3;
    }

    protected static MutableComponent confirmMessage(boolean boolean0, String string1) {
        return confirmMessage(boolean0).append(CommonComponents.SPACE).append(Component.literal(string1));
    }

    protected static MutableComponent confirmMessage(boolean boolean0) {
        return Component.translatable(boolean0 ? "chat.link.confirmTrusted" : "chat.link.confirm");
    }

    @Override
    protected void addButtons(int int0) {
        this.m_142416_(Button.builder(this.f_95647_, p_169249_ -> this.f_95649_.accept(true)).bounds(this.f_96543_ / 2 - 50 - 105, int0, 100, 20).build());
        this.m_142416_(Button.builder(COPY_BUTTON_TEXT, p_169247_ -> {
            this.copyToClipboard();
            this.f_95649_.accept(false);
        }).bounds(this.f_96543_ / 2 - 50, int0, 100, 20).build());
        this.m_142416_(Button.builder(this.f_95648_, p_169245_ -> this.f_95649_.accept(false)).bounds(this.f_96543_ / 2 - 50 + 105, int0, 100, 20).build());
    }

    public void copyToClipboard() {
        this.f_96541_.keyboardHandler.setClipboard(this.url);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        super.render(guiGraphics0, int1, int2, float3);
        if (this.showWarning) {
            guiGraphics0.drawCenteredString(this.f_96547_, WARNING_TEXT, this.f_96543_ / 2, 110, 16764108);
        }
    }

    public static void confirmLinkNow(String string0, Screen screen1, boolean boolean2) {
        Minecraft $$3 = Minecraft.getInstance();
        $$3.setScreen(new ConfirmLinkScreen(p_274671_ -> {
            if (p_274671_) {
                Util.getPlatform().openUri(string0);
            }
            $$3.setScreen(screen1);
        }, string0, boolean2));
    }

    public static Button.OnPress confirmLink(String string0, Screen screen1, boolean boolean2) {
        return p_274667_ -> confirmLinkNow(string0, screen1, boolean2);
    }
}