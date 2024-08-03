package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class DisconnectedRealmsScreen extends RealmsScreen {

    private final Component reason;

    private MultiLineLabel message = MultiLineLabel.EMPTY;

    private final Screen parent;

    private int textHeight;

    public DisconnectedRealmsScreen(Screen screen0, Component component1, Component component2) {
        super(component1);
        this.parent = screen0;
        this.reason = component2;
    }

    @Override
    public void init() {
        Minecraft $$0 = Minecraft.getInstance();
        $$0.setConnectedToRealms(false);
        $$0.getDownloadedPackSource().clearServerPack();
        this.message = MultiLineLabel.create(this.f_96547_, this.reason, this.f_96543_ - 50);
        this.textHeight = this.message.getLineCount() * 9;
        this.m_142416_(Button.builder(CommonComponents.GUI_BACK, p_120663_ -> $$0.setScreen(this.parent)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 2 + this.textHeight / 2 + 9, 200, 20).build());
    }

    @Override
    public Component getNarrationMessage() {
        return Component.empty().append(this.f_96539_).append(": ").append(this.reason);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, this.f_96544_ / 2 - this.textHeight / 2 - 9 * 2, 11184810);
        this.message.renderCentered(guiGraphics0, this.f_96543_ / 2, this.f_96544_ / 2 - this.textHeight / 2);
        super.m_88315_(guiGraphics0, int1, int2, float3);
    }
}