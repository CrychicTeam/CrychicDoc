package net.minecraft.client.gui.screens;

import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class SymlinkWarningScreen extends Screen {

    private static final Component TITLE = Component.translatable("symlink_warning.title").withStyle(ChatFormatting.BOLD);

    private static final Component MESSAGE_TEXT = Component.translatable("symlink_warning.message", "https://aka.ms/MinecraftSymLinks");

    @Nullable
    private final Screen callbackScreen;

    private final GridLayout layout = new GridLayout().rowSpacing(10);

    public SymlinkWarningScreen(@Nullable Screen screen0) {
        super(TITLE);
        this.callbackScreen = screen0;
    }

    @Override
    protected void init() {
        super.init();
        this.layout.defaultCellSetting().alignHorizontallyCenter();
        GridLayout.RowHelper $$0 = this.layout.createRowHelper(1);
        $$0.addChild(new StringWidget(this.f_96539_, this.f_96547_));
        $$0.addChild(new MultiLineTextWidget(MESSAGE_TEXT, this.f_96547_).setMaxWidth(this.f_96543_ - 50).setCentered(true));
        int $$1 = 120;
        GridLayout $$2 = new GridLayout().columnSpacing(5);
        GridLayout.RowHelper $$3 = $$2.createRowHelper(3);
        $$3.addChild(Button.builder(CommonComponents.GUI_OPEN_IN_BROWSER, p_289977_ -> Util.getPlatform().openUri("https://aka.ms/MinecraftSymLinks")).size(120, 20).build());
        $$3.addChild(Button.builder(CommonComponents.GUI_COPY_LINK_TO_CLIPBOARD, p_289939_ -> this.f_96541_.keyboardHandler.setClipboard("https://aka.ms/MinecraftSymLinks")).size(120, 20).build());
        $$3.addChild(Button.builder(CommonComponents.GUI_BACK, p_289963_ -> this.onClose()).size(120, 20).build());
        $$0.addChild($$2);
        this.repositionElements();
        this.layout.m_264134_(this::m_142416_);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        FrameLayout.centerInRectangle(this.layout, this.m_264198_());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public Component getNarrationMessage() {
        return CommonComponents.joinForNarration(super.getNarrationMessage(), MESSAGE_TEXT);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.callbackScreen);
    }
}