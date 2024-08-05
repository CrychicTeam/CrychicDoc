package net.minecraft.client.gui.screens;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class DirectJoinServerScreen extends Screen {

    private static final Component ENTER_IP_LABEL = Component.translatable("addServer.enterIp");

    private Button selectButton;

    private final ServerData serverData;

    private EditBox ipEdit;

    private final BooleanConsumer callback;

    private final Screen lastScreen;

    public DirectJoinServerScreen(Screen screen0, BooleanConsumer booleanConsumer1, ServerData serverData2) {
        super(Component.translatable("selectServer.direct"));
        this.lastScreen = screen0;
        this.serverData = serverData2;
        this.callback = booleanConsumer1;
    }

    @Override
    public void tick() {
        this.ipEdit.tick();
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (!this.selectButton.f_93623_ || this.m_7222_() != this.ipEdit || int0 != 257 && int0 != 335) {
            return super.keyPressed(int0, int1, int2);
        } else {
            this.onSelect();
            return true;
        }
    }

    @Override
    protected void init() {
        this.ipEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 100, 116, 200, 20, Component.translatable("addServer.enterIp"));
        this.ipEdit.setMaxLength(128);
        this.ipEdit.setValue(this.f_96541_.options.lastMpIp);
        this.ipEdit.setResponder(p_95983_ -> this.updateSelectButtonStatus());
        this.m_7787_(this.ipEdit);
        this.selectButton = (Button) this.m_142416_(Button.builder(Component.translatable("selectServer.select"), p_95981_ -> this.onSelect()).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 96 + 12, 200, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_95977_ -> this.callback.accept(false)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 120 + 12, 200, 20).build());
        this.m_264313_(this.ipEdit);
        this.updateSelectButtonStatus();
    }

    @Override
    public void resize(Minecraft minecraft0, int int1, int int2) {
        String $$3 = this.ipEdit.getValue();
        this.m_6575_(minecraft0, int1, int2);
        this.ipEdit.setValue($$3);
    }

    private void onSelect() {
        this.serverData.ip = this.ipEdit.getValue();
        this.callback.accept(true);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    @Override
    public void removed() {
        this.f_96541_.options.lastMpIp = this.ipEdit.getValue();
        this.f_96541_.options.save();
    }

    private void updateSelectButtonStatus() {
        this.selectButton.f_93623_ = ServerAddress.isValidAddress(this.ipEdit.getValue());
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 20, 16777215);
        guiGraphics0.drawString(this.f_96547_, ENTER_IP_LABEL, this.f_96543_ / 2 - 100, 100, 10526880);
        this.ipEdit.m_88315_(guiGraphics0, int1, int2, float3);
        super.render(guiGraphics0, int1, int2, float3);
    }
}