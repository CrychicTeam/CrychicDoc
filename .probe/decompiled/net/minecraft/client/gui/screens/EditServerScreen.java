package net.minecraft.client.gui.screens;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class EditServerScreen extends Screen {

    private static final Component NAME_LABEL = Component.translatable("addServer.enterName");

    private static final Component IP_LABEL = Component.translatable("addServer.enterIp");

    private Button addButton;

    private final BooleanConsumer callback;

    private final ServerData serverData;

    private EditBox ipEdit;

    private EditBox nameEdit;

    private final Screen lastScreen;

    public EditServerScreen(Screen screen0, BooleanConsumer booleanConsumer1, ServerData serverData2) {
        super(Component.translatable("addServer.title"));
        this.lastScreen = screen0;
        this.callback = booleanConsumer1;
        this.serverData = serverData2;
    }

    @Override
    public void tick() {
        this.nameEdit.tick();
        this.ipEdit.tick();
    }

    @Override
    protected void init() {
        this.nameEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 100, 66, 200, 20, Component.translatable("addServer.enterName"));
        this.nameEdit.setValue(this.serverData.name);
        this.nameEdit.setResponder(p_169304_ -> this.updateAddButtonStatus());
        this.m_7787_(this.nameEdit);
        this.ipEdit = new EditBox(this.f_96547_, this.f_96543_ / 2 - 100, 106, 200, 20, Component.translatable("addServer.enterIp"));
        this.ipEdit.setMaxLength(128);
        this.ipEdit.setValue(this.serverData.ip);
        this.ipEdit.setResponder(p_169302_ -> this.updateAddButtonStatus());
        this.m_7787_(this.ipEdit);
        this.m_142416_(CycleButton.<ServerData.ServerPackStatus>builder(ServerData.ServerPackStatus::m_105400_).withValues(ServerData.ServerPackStatus.values()).withInitialValue(this.serverData.getResourcePackStatus()).create(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 72, 200, 20, Component.translatable("addServer.resourcePack"), (p_169299_, p_169300_) -> this.serverData.setResourcePackStatus(p_169300_)));
        this.addButton = (Button) this.m_142416_(Button.builder(Component.translatable("addServer.add"), p_96030_ -> this.onAdd()).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 96 + 18, 200, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_169297_ -> this.callback.accept(false)).bounds(this.f_96543_ / 2 - 100, this.f_96544_ / 4 + 120 + 18, 200, 20).build());
        this.m_264313_(this.nameEdit);
        this.updateAddButtonStatus();
    }

    @Override
    public void resize(Minecraft minecraft0, int int1, int int2) {
        String $$3 = this.ipEdit.getValue();
        String $$4 = this.nameEdit.getValue();
        this.m_6575_(minecraft0, int1, int2);
        this.ipEdit.setValue($$3);
        this.nameEdit.setValue($$4);
    }

    private void onAdd() {
        this.serverData.name = this.nameEdit.getValue();
        this.serverData.ip = this.ipEdit.getValue();
        this.callback.accept(true);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    private void updateAddButtonStatus() {
        this.addButton.f_93623_ = ServerAddress.isValidAddress(this.ipEdit.getValue()) && !this.nameEdit.getValue().isEmpty();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 17, 16777215);
        guiGraphics0.drawString(this.f_96547_, NAME_LABEL, this.f_96543_ / 2 - 100, 53, 10526880);
        guiGraphics0.drawString(this.f_96547_, IP_LABEL, this.f_96543_ / 2 - 100, 94, 10526880);
        this.nameEdit.m_88315_(guiGraphics0, int1, int2, float3);
        this.ipEdit.m_88315_(guiGraphics0, int1, int2, float3);
        super.render(guiGraphics0, int1, int2, float3);
    }
}