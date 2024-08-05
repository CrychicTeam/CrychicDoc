package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class BackupConfirmScreen extends Screen {

    private final Screen lastScreen;

    protected final BackupConfirmScreen.Listener listener;

    private final Component description;

    private final boolean promptForCacheErase;

    private MultiLineLabel message = MultiLineLabel.EMPTY;

    protected int id;

    private Checkbox eraseCache;

    public BackupConfirmScreen(Screen screen0, BackupConfirmScreen.Listener backupConfirmScreenListener1, Component component2, Component component3, boolean boolean4) {
        super(component2);
        this.lastScreen = screen0;
        this.listener = backupConfirmScreenListener1;
        this.description = component3;
        this.promptForCacheErase = boolean4;
    }

    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.f_96547_, this.description, this.f_96543_ - 50);
        int $$0 = (this.message.getLineCount() + 1) * 9;
        this.m_142416_(Button.builder(Component.translatable("selectWorld.backupJoinConfirmButton"), p_95564_ -> this.listener.proceed(true, this.eraseCache.selected())).bounds(this.f_96543_ / 2 - 155, 100 + $$0, 150, 20).build());
        this.m_142416_(Button.builder(Component.translatable("selectWorld.backupJoinSkipButton"), p_95562_ -> this.listener.proceed(false, this.eraseCache.selected())).bounds(this.f_96543_ / 2 - 155 + 160, 100 + $$0, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280786_ -> this.f_96541_.setScreen(this.lastScreen)).bounds(this.f_96543_ / 2 - 155 + 80, 124 + $$0, 150, 20).build());
        this.eraseCache = new Checkbox(this.f_96543_ / 2 - 155 + 80, 76 + $$0, 150, 20, Component.translatable("selectWorld.backupEraseCache"), false);
        if (this.promptForCacheErase) {
            this.m_142416_(this.eraseCache);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 50, 16777215);
        this.message.renderCentered(guiGraphics0, this.f_96543_ / 2, 70);
        super.render(guiGraphics0, int1, int2, float3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.f_96541_.setScreen(this.lastScreen);
            return true;
        } else {
            return super.keyPressed(int0, int1, int2);
        }
    }

    public interface Listener {

        void proceed(boolean var1, boolean var2);
    }
}